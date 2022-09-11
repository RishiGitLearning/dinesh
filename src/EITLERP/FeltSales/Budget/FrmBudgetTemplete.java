/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales.Budget;

import EITLERP.EITLTableModel;
import EITLERP.JTextFieldHint;
import EITLERP.LOV;
import EITLERP.data;
import java.awt.Desktop;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author Dharmendra
 */
public class FrmBudgetTemplete extends javax.swing.JApplet {

    /**
     * Initializes the applet FrmBudgetTemplete
     */
    private EITLTableModel DataModelBudget = new EITLTableModel();
    public EITLERP.FeltSales.Budget.clsExcelExporter exprt = new EITLERP.FeltSales.Budget.clsExcelExporter();

    @Override
    public void init() {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        
        //</editor-fold>

        /* Create and display the applet */
        initComponents();
        file1.setVisible(false);
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();                
        int scrwidth = gd.getDisplayMode().getWidth();
        int scrheight = gd.getDisplayMode().getHeight();
        setSize(scrwidth, scrheight);
        //txtpartyname.setEditable(false);
    }

    /**
     * This method is called from within the init() method to initialize the
     * form. WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Budget = new javax.swing.JPanel();
        cmdShowBudget = new javax.swing.JButton();
        jScrollPane33 = new javax.swing.JScrollPane();
        Tablebudget = new javax.swing.JTable();
        cmdbudgetExporttoExcel1 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        txtpartycode = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        txtlengthfrom = new javax.swing.JTextField();
        txtlengthto = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        txtwidthfrom = new javax.swing.JTextField();
        txtwidthto = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        txtgsqfrom = new javax.swing.JTextField();
        txtgsqto = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        txtproductcode = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtMachineNo = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtPosition = new javax.swing.JTextField();
        CLR_BTN = new javax.swing.JButton();
        file1 = new javax.swing.JFileChooser();
        jLabel36 = new javax.swing.JLabel();
        txtyearfrom = new javax.swing.JTextField();
        lblyearto = new javax.swing.JLabel();

        Budget.setToolTipText("Contacts");
        Budget.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        Budget.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                BudgetMouseEntered(evt);
            }
        });
        Budget.setLayout(null);

        cmdShowBudget.setText("Generate Templete");
        cmdShowBudget.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdShowBudgetActionPerformed(evt);
            }
        });
        Budget.add(cmdShowBudget);
        cmdShowBudget.setBounds(220, 100, 160, 40);

        Tablebudget.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane33.setViewportView(Tablebudget);

        Budget.add(jScrollPane33);
        jScrollPane33.setBounds(0, 150, 950, 330);

        cmdbudgetExporttoExcel1.setText("Export to Excel");
        cmdbudgetExporttoExcel1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdbudgetExporttoExcel1ActionPerformed(evt);
            }
        });
        Budget.add(cmdbudgetExporttoExcel1);
        cmdbudgetExporttoExcel1.setBounds(10, 490, 150, 40);

        jLabel3.setText("Party Code");
        Budget.add(jLabel3);
        jLabel3.setBounds(10, 10, 70, 20);

        txtpartycode.setToolTipText("Press F1 key for search Party Code");
        txtpartycode = new JTextFieldHint(new JTextField(),"Search by F1");
        txtpartycode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtpartycodeFocusLost(evt);
            }
        });
        txtpartycode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtpartycodeKeyPressed(evt);
            }
        });
        Budget.add(txtpartycode);
        txtpartycode.setBounds(90, 0, 600, 40);

        jLabel18.setText("Length (M)");
        Budget.add(jLabel18);
        jLabel18.setBounds(10, 40, 80, 20);

        txtlengthfrom = new JTextFieldHint(new JTextField(),"From");
        Budget.add(txtlengthfrom);
        txtlengthfrom.setBounds(90, 40, 50, 30);

        txtlengthto = new JTextFieldHint(new JTextField(),"To");
        Budget.add(txtlengthto);
        txtlengthto.setBounds(150, 40, 50, 30);

        jLabel19.setText("Width (M)");
        Budget.add(jLabel19);
        jLabel19.setBounds(10, 70, 80, 20);

        txtwidthfrom = new JTextFieldHint(new JTextField(),"From");
        Budget.add(txtwidthfrom);
        txtwidthfrom.setBounds(90, 70, 50, 30);

        txtwidthto = new JTextFieldHint(new JTextField(),"To");
        Budget.add(txtwidthto);
        txtwidthto.setBounds(150, 70, 50, 30);

        jLabel20.setText("GSM");
        Budget.add(jLabel20);
        jLabel20.setBounds(10, 100, 40, 20);

        txtgsqfrom = new JTextFieldHint(new JTextField(),"From");
        Budget.add(txtgsqfrom);
        txtgsqfrom.setBounds(90, 100, 50, 30);

        txtgsqto = new JTextFieldHint(new JTextField(),"To");
        Budget.add(txtgsqto);
        txtgsqto.setBounds(150, 100, 50, 30);

        jLabel35.setText("Product Code");
        Budget.add(jLabel35);
        jLabel35.setBounds(220, 40, 90, 20);

        txtproductcode.setToolTipText("Press F1 key to search Product Code");
        txtproductcode = new JTextFieldHint(new JTextField(),"Search By F1");
        txtproductcode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtproductcodeActionPerformed(evt);
            }
        });
        txtproductcode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtproductcodeKeyPressed(evt);
            }
        });
        Budget.add(txtproductcode);
        txtproductcode.setBounds(310, 40, 110, 30);

        jLabel4.setText("Machine No");
        Budget.add(jLabel4);
        jLabel4.setBounds(220, 70, 80, 20);

        txtMachineNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtMachineNoKeyPressed(evt);
            }
        });
        Budget.add(txtMachineNo);
        txtMachineNo.setBounds(310, 70, 50, 30);

        jLabel6.setText("Position");
        Budget.add(jLabel6);
        jLabel6.setBounds(410, 70, 50, 20);
        Budget.add(txtPosition);
        txtPosition.setBounds(460, 70, 50, 30);

        CLR_BTN.setText("Clear All");
        CLR_BTN.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        CLR_BTN.setMargin(new java.awt.Insets(2, 7, 2, 7));
        CLR_BTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CLR_BTNActionPerformed(evt);
            }
        });
        Budget.add(CLR_BTN);
        CLR_BTN.setBounds(390, 100, 130, 40);
        Budget.add(file1);
        file1.setBounds(120, 60, 582, 397);

        jLabel36.setText("Year[YYYY]");
        Budget.add(jLabel36);
        jLabel36.setBounds(430, 40, 90, 20);

        txtyearfrom.setToolTipText("Press F1 key to search Product Code");
        txtproductcode = new JTextFieldHint(new JTextField(),"Search By F1");
        txtyearfrom.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtyearfromFocusLost(evt);
            }
        });
        Budget.add(txtyearfrom);
        txtyearfrom.setBounds(520, 40, 60, 30);

        lblyearto.setText("YYYY");
        Budget.add(lblyearto);
        lblyearto.setBounds(590, 40, 50, 30);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 965, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(Budget, javax.swing.GroupLayout.PREFERRED_SIZE, 965, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 640, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(Budget, javax.swing.GroupLayout.PREFERRED_SIZE, 640, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cmdShowBudgetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdShowBudgetActionPerformed
        // TODO add your handling code here:
        boolean generate = false;
        if (txtyearfrom.getText().trim().length() >= 4) {
            generate = true;
        } else {
            JOptionPane.showMessageDialog(this, "Please Enter Year for Budget", "ERROR", JOptionPane.ERROR_MESSAGE);
            generate = false;
            txtyearfrom.requestFocus();
        }
        if (generate) {
            GenerateBudgetTemplete();
        }
    }//GEN-LAST:event_cmdShowBudgetActionPerformed

    private void cmdbudgetExporttoExcel1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdbudgetExporttoExcel1ActionPerformed
        // TODO add your handling code here:
        try {
            File file = null;
            file1.setVisible(true);
            int returnVal = file1.showSaveDialog(this);
            if (returnVal == file1.APPROVE_OPTION) {
                file = file1.getSelectedFile();
            }
            file1.setVisible(false);

            exprt.fillData(Tablebudget, new File(file1.getSelectedFile().toString() + ".xls"), "Budget");
            JOptionPane.showMessageDialog(null, "Data saved at "
                    + file.toString() + " successfully ...", "Message",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_cmdbudgetExporttoExcel1ActionPerformed

    private void BudgetMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BudgetMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_BudgetMouseEntered

    private void txtpartycodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtpartycodeFocusLost
        try {
//            if (!txtpartycode.getText().equals("")) {
//                String strSQL = "";
//                ResultSet rsTmp;
//                strSQL = "";
//                strSQL += "SELECT PARTY_NAME,ADDRESS1,ADDRESS2,DISPATCH_STATION,INSURANCE_CODE FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE='210010' AND PARTY_CODE = " + txtpartycode.getText().trim() + "";
//                rsTmp = data.getResult(strSQL);
//                rsTmp.first();
//                txtpartyname.setText(rsTmp.getString("PARTY_NAME"));
            //}
        } catch (Exception e) {
        }

    }//GEN-LAST:event_txtpartycodeFocusLost

    private void txtpartycodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtpartycodeKeyPressed
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            BudgetLOV aList = new BudgetLOV();
            //aList.SQL="SELECT PARTY_CODE,NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER ORDER BY NAME";
            aList.SQL = "SELECT NULL,PARTY_CODE,PARTY_NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE='210010' AND LEFT(PARTY_CODE,1)='8' ";
            aList.ReturnCol = 1;
            aList.ShowReturnCol = true;
            aList.DefaultSearchOn = 2;
            //aList.DefaultSearchOn=1;

            if (aList.ShowLOV()) {
                txtpartycode.setText(aList.ReturnVal);
                //txtpartyname.setText(clsSales_Part.getPartyName(EITLERPGLOBAL.gCompanyID,aList.ReturnVal));
            }
        }
    }//GEN-LAST:event_txtpartycodeKeyPressed

    private void txtproductcodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtproductcodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtproductcodeActionPerformed

    private void txtproductcodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtproductcodeKeyPressed

        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            LOV aList = new LOV();
            //aList.SQL="SELECT PARTY_CODE,NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER ORDER BY NAME";
            aList.SQL = "SELECT SUBSTRING(ITEM_CODE,1,6) AS ITEM_CODE,ITEM_DESC,GRUP FROM PRODUCTION.FELT_RATE_MASTER ORDER BY ITEM_CODE ";
            aList.ReturnCol = 1;
            aList.ShowReturnCol = true;
            aList.DefaultSearchOn = 2;
            if (aList.ShowLOV()) {
                //txtpartycode.setText(aList.ReturnVal);
                //txtpartyname.setText(clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID,aList.ReturnVal));
                //txtproductcode.setText(aList.ReturnVal);
                txtproductcode.setText(txtproductcode.getText() + aList.ReturnVal);

            }
        }
    }//GEN-LAST:event_txtproductcodeKeyPressed

    private void txtMachineNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMachineNoKeyPressed

    }//GEN-LAST:event_txtMachineNoKeyPressed

    private void CLR_BTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CLR_BTNActionPerformed
//        txtpartyname.setText("");
        txtpartycode.setText("");
        txtlengthfrom.setText("");
        txtlengthto.setText("");
        txtwidthfrom.setText("");
        txtwidthto.setText("");
        txtgsqfrom.setText("");
        txtgsqto.setText("");
        txtproductcode.setText("");
        txtMachineNo.setText("");
        txtPosition.setText("");

//        txtpartyname.setEnabled(true);
//        txtpartyname.setEditable(false);
        txtpartycode.setEnabled(true);
        txtlengthfrom.setEnabled(true);
        txtlengthto.setEnabled(true);
        txtwidthfrom.setEnabled(true);
        txtwidthto.setEnabled(true);
        txtgsqfrom.setEnabled(true);
        txtgsqto.setEnabled(true);
        txtproductcode.setEnabled(true);
        txtMachineNo.setEnabled(true);
        txtPosition.setEnabled(true);
    }//GEN-LAST:event_CLR_BTNActionPerformed

    private void txtyearfromFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtyearfromFocusLost
        // TODO add your handling code here:
        try {
            lblyearto.setText(String.valueOf(Integer.parseInt(txtyearfrom.getText()) + 1));
        } catch (Exception e) {
            txtyearfrom.setText("");
            e.printStackTrace();
        }
    }//GEN-LAST:event_txtyearfromFocusLost


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Budget;
    private javax.swing.JButton CLR_BTN;
    private javax.swing.JTable Tablebudget;
    private javax.swing.JButton cmdShowBudget;
    private javax.swing.JButton cmdbudgetExporttoExcel1;
    private javax.swing.JFileChooser file1;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane33;
    private javax.swing.JLabel lblyearto;
    private javax.swing.JTextField txtMachineNo;
    private javax.swing.JTextField txtPosition;
    private javax.swing.JTextField txtgsqfrom;
    private javax.swing.JTextField txtgsqto;
    private javax.swing.JTextField txtlengthfrom;
    private javax.swing.JTextField txtlengthto;
    private javax.swing.JTextField txtpartycode;
    private javax.swing.JTextField txtproductcode;
    private javax.swing.JTextField txtwidthfrom;
    private javax.swing.JTextField txtwidthto;
    private javax.swing.JTextField txtyearfrom;
    // End of variables declaration//GEN-END:variables
 private void GenerateBudgetTemplete() {
        DataModelBudget = new EITLTableModel();
        Tablebudget.removeAll();
        Tablebudget.setModel(DataModelBudget);
        Tablebudget.setAutoResizeMode(Tablebudget.AUTO_RESIZE_OFF);

        try {
            String sql, cndtn = " LEFT(PARTY_CODE,1)='8' ";
            ResultSet rs;
            String PartyCode = txtpartycode.getText().toString();
            String ItemCode = txtproductcode.getText().toString();
            String Lengthfrom = txtlengthfrom.getText().toString();
            String Lengthto = txtlengthto.getText().toString();
            String Widthfrom = txtwidthfrom.getText().toString();
            String Widthto = txtwidthto.getText().toString();
            String GSMfrom = txtgsqfrom.getText().toString();
            String GSMto = txtgsqto.getText().toString();
            String MachineNo = txtMachineNo.getText().toString();
            String PositionNo = txtPosition.getText().toString();

            if (!txtpartycode.getText().equals("")) {
                if (cndtn.trim().length() > 0) {
                    cndtn += " AND PARTY_CODE IN( " + PartyCode + ")";
                } else {
                    cndtn += "  PARTY_CODE IN( " + PartyCode + ")";
                }
            }
            if (!txtproductcode.getText().equals("")) {
                if (cndtn.trim().length() > 0) {
                    cndtn += " AND MM_ITEM_CODE LIKE '" + ItemCode + "%'";
                } else {
                    cndtn += "  MM_ITEM_CODE LIKE '" + ItemCode + "%'";
                }
            }

            if (!txtlengthfrom.getText().equals("")) {
                if (cndtn.trim().length() > 0) {
                    cndtn += " AND MM_FELT_LENGTH>=" + Lengthfrom + " OR MM_FABRIC_LENGTH>=" + Lengthfrom;
                } else {
                    cndtn += "  MM_FELT_LENGTH>='" + Lengthfrom + " OR MM_FABRIC_LENGTH>=" + Lengthfrom;;
                }
            }
            if (!txtlengthto.getText().equals("")) {
                if (cndtn.trim().length() > 0) {
                    cndtn += " AND MM_FELT_LENGTH<=" + Lengthto + " OR MM_FABRIC_LENGTH<=" + Lengthto;
                } else {
                    cndtn += "  MM_FELT_LENGTH<='" + Lengthto + " OR MM_FABRIC_LENGTH<=" + Lengthto;
                }
            }
            if (!txtwidthfrom.getText().equals("")) {
                if (cndtn.trim().length() > 0) {
                    cndtn += " AND MM_FELT_WIDTH>=" + Widthfrom + " OR MM_FABRIC_WIDTH>=" + Widthfrom;
                } else {
                    cndtn += "  MM_FELT_WIDTH>='" + Widthfrom + " OR MM_FABRIC_WIDTH>=" + Widthfrom;
                }
            }
            if (!txtwidthto.getText().equals("")) {
                if (cndtn.trim().length() > 0) {
                    cndtn += " AND MM_FELT_WIDTH<=" + Widthto + " OR MM_FABRIC_WIDTH<=" + Widthto;
                } else {
                    cndtn += "  MM_FELT_WIDTH<='" + Widthto + " OR MM_FABRIC_WIDTH<=" + Widthto;
                }
            }
            if (!txtgsqfrom.getText().equals("")) {
                if (cndtn.trim().length() > 0) {
                    cndtn += " AND MM_FELT_GSM>=" + GSMfrom;
                } else {
                    cndtn += "  MM_FELT_GSM>='" + GSMfrom;
                }
            }
            if (!txtgsqto.getText().equals("")) {
                if (cndtn.trim().length() > 0) {
                    cndtn += " AND MM_FELT_GSM<=" + GSMfrom;
                } else {
                    cndtn += "  MM_FELT_GSM<='" + GSMfrom;
                }
            }
            if (!txtMachineNo.getText().equals("")) {
                if (cndtn.trim().length() > 0) {
                    cndtn += " AND MACHINE_NO='" + MachineNo + "'";
                } else {
                    cndtn += "  MACHINE_NO='" + MachineNo + "'";
                }
            }
            if (!txtPosition.getText().equals("")) {
                if (cndtn.trim().length() > 0) {
                    cndtn += " AND MM_MACHINE_POSITION='" + PositionNo + "'";
                } else {
                    cndtn += "  MM_MACHINE_POSITION='" + PositionNo + "'";
                }
            }
            if (cndtn.trim().length() > 0) {
                cndtn = " WHERE " + cndtn;
            }
            sql = "SELECT DISTINCT PARTY_CODE,PARTY_NAME,MACHINE_NO,MM_MACHINE_POSITION AS POSITION,POSITION_DESC,MM_FELT_STYLE AS STYLE,MM_UPN_NO AS UPN,"
                    + "ROUND(MM_FELT_LENGTH*1,2) AS PRESS_LENGTH,ROUND(MM_FELT_WIDTH*1,2) AS PRESS_WIDTH,ROUND(MM_FELT_GSM*1,2) AS PRESS_GSM,"
                    + "ROUND(MM_TH_WEIGHT*1,2) AS PRESS_WEIGHT,ROUND(AREA_SQMTR*1,2) AS PRESS_SQMTR,"
                    + "ROUND(MM_FABRIC_LENGTH*1,2) DRY_LENGTH,ROUND(MM_FABRIC_WIDTH*1,2) AS DRY_WIDTH,ROUND(MM_SIZE_M2*1,2) AS DRY_SQMTR,"
                    + "ROUND(MM_FABRIC_TH_WEIGHT*1,2) AS DRY_WEIGHT,MM_ITEM_CODE AS QUALITY_NO,GROUP_NAME,"
                    + "ROUND(FELT_RATE*1,2) AS SELLING_PRICE,ROUND(COALESCE(DISC_PER,0)*1,2) AS SP_DISCOUNT,ROUND(COALESCE(WIP,0)*1,2) AS WIP,ROUND(COALESCE(STOCK,0)*1,2) AS STOCK,"
                    + "'' AS Q1,'' AS Q2,'' AS Q3,'' AS Q4,";
            if (PartyCode.length() > 0) {
                sql = sql + "'' AS PARTY_STATUS,";
            } else {
                sql = sql + "'BP' AS PARTY_STATUS,";
            }
            sql = sql + "COALESCE(PARTY_STATUS,'NEW') AS SYSTEM_STATUS,'' AS REMARKS "
                    + "FROM "
                    + "(SELECT A.MM_UPN_NO,A.PARTY_CODE,PARTY_NAME,A.MACHINE_NO,MM_MACHINE_POSITION,POSITION_DESC,MM_FELT_STYLE,"
                    + "MM_FELT_LENGTH,MM_FELT_WIDTH,MM_FELT_GSM,MM_TH_WEIGHT,AREA_SQMTR,MM_FABRIC_LENGTH,"
                    + "MM_FABRIC_WIDTH,MM_SIZE_M2,MM_FABRIC_TH_WEIGHT,MM_ITEM_CODE,GROUP_NAME,FELT_RATE,"
                    + "DISC_PER  FROM "
                    + "( "
                    + "SELECT D.MM_UPN_NO,D.MM_PARTY_CODE AS PARTY_CODE,PARTY_NAME,D.MM_MACHINE_NO AS MACHINE_NO,"
                    + "MM_MACHINE_POSITION,POSITION_DESC,MM_FELT_STYLE,MM_FELT_LENGTH,MM_FELT_WIDTH,MM_FELT_GSM,"
                    + "(MM_FELT_LENGTH*MM_FELT_WIDTH*MM_FELT_GSM/1000) AS MM_TH_WEIGHT,"
                    + "MM_FELT_LENGTH*MM_FELT_WIDTH AS AREA_SQMTR,MM_FABRIC_LENGTH,MM_FABRIC_WIDTH,MM_SIZE_M2,"
                    + "(MM_FABRIC_LENGTH*MM_FABRIC_WIDTH*MM_FELT_GSM/1000) AS MM_FABRIC_TH_WEIGHT,"
                    + "MM_ITEM_CODE,GROUP_NAME,"
                    + "CASE WHEN WT_RATE =0 THEN SQM_CHRG ELSE WT_RATE END  AS FELT_RATE "
                    + "FROM PRODUCTION.FELT_MACHINE_MASTER_HEADER H,  "
                    + "PRODUCTION.FELT_MACHINE_MASTER_DETAIL D, "
                    + "PRODUCTION.FELT_QLT_RATE_MASTER R, "
                    + "DINESHMILLS.D_SAL_PARTY_MASTER P , "
                    + "PRODUCTION.FELT_MACHINE_POSITION_MST MP "
                    + "WHERE H.MM_DOC_NO=D.MM_DOC_NO AND H.APPROVED=1 AND H.CANCELED=0  "
                    + "AND MM_ITEM_CODE = PRODUCT_CODE  AND EFFECTIVE_TO ='0000-00-00'  "
                    + "AND P.PARTY_CODE = H.MM_PARTY_CODE AND POSITION_NO = MM_MACHINE_POSITION "
                    + ")  AS A  "
                    + "LEFT JOIN "
                    + "( SELECT * FROM  "
                    + "(SELECT PARTY_CODE,PRODUCT_CODE,DISC_PER,EFFECTIVE_FROM,EFFECTIVE_TO, "
                    + "CASE WHEN EFFECTIVE_TO IS NULL THEN COALESCE(EFFECTIVE_TO,CURDATE()) "
                    + "WHEN EFFECTIVE_TO ='0000-00-00' THEN COALESCE(EFFECTIVE_TO,CURDATE()) ELSE "
                    + "EFFECTIVE_TO END AS EFFECTIVE_TILL_DATE "
                    + " FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL ) AS RDM "
                    + "WHERE CURDATE() BETWEEN EFFECTIVE_FROM AND EFFECTIVE_TILL_DATE "
                    + ") AS B "
                    + "ON A.MM_ITEM_CODE=B.PRODUCT_CODE AND A.PARTY_CODE=B.PARTY_CODE) AS MM "
                    + "LEFT JOIN  "
                    + "(SELECT PR_PARTY_CODE,PR_MACHINE_NO,PR_POSITION_NO,PR_PRODUCT_CODE, "
                    + "SUM(COALESCE(CASE WHEN PR_PIECE_STAGE IN ('WEAVING','MENDING','NEEDLING','FONISHING') THEN 1 END,0)) AS WIP, "
                    + "SUM(COALESCE(CASE WHEN PR_PIECE_STAGE IN ('IN STOCK','BSR') THEN 1 END,0)) AS STOCK "
                    + "FROM PRODUCTION.FELT_SALES_PIECE_REGISTER "
                    + "WHERE  PR_PRIORITY_HOLD_CAN_FLAG IN (0,1,2,3,4,5) "
                    + "GROUP BY PR_PARTY_CODE,PR_MACHINE_NO,PR_POSITION_NO,PR_PRODUCT_CODE) AS PR "
                    + "ON MM_ITEM_CODE=PR_PRODUCT_CODE AND PARTY_CODE=PR_PARTY_CODE "
                    + "AND PR_MACHINE_NO = MACHINE_NO AND PR_POSITION_NO = MM_MACHINE_POSITION "
                    + "LEFT JOIN (SELECT PARTY_CODE AS B_PARTY_CODE,PARTY_STATUS FROM PRODUCTION.FELT_BUDGET "
                    + "WHERE YEAR_FROM='"+txtyearfrom.getText()+"') AS BUDJET "
                    + "ON PARTY_CODE=B_PARTY_CODE "
                    + cndtn
                    + " ORDER BY PARTY_CODE,MM_ITEM_CODE,MACHINE_NO,MM_MACHINE_POSITION,MM_ITEM_CODE";
            System.out.println(sql);

            rs = data.getResult(sql);
            ResultSetMetaData rsInfo = rs.getMetaData();

            //Format the table from the resultset meta data
            int i = 1;
            for (i = 1; i <= rsInfo.getColumnCount(); i++) {
                DataModelBudget.addColumn(rsInfo.getColumnName(i));
            }
            rs.first();
            System.out.println("Row no." + rs.getRow());
            if (rs.getRow() > 0) {
                while (!rs.isAfterLast()) {
                    Object[] rowData = new Object[100];
                    for (int m = 1; m < i; m++) {
                        if (m == 26) {
                            if (rs.getString(m + 1).equalsIgnoreCase("NEW")) {
                                rowData[m - 1] = rs.getString(m);
                            } else {
                                rowData[m - 1] = "";
                            }
                        } else {
                            rowData[m - 1] = rs.getString(m);
                        }
                    }
                    DataModelBudget.addRow(rowData);
                    rs.next();
                }
            }
            DataModelBudget.TableReadOnly(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}