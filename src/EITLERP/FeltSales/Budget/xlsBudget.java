/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales.Budget;

import EITLERP.EITLERPGLOBAL;
import EITLERP.EITLTableModel;
import EITLERP.FeltSales.FeltFinishing.ReportRegister;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.HashMap;
import javax.swing.JOptionPane;
import sdml.felt.commonUI.data;

/**
 *
 * @author Dharmendra
 */
public class xlsBudget extends javax.swing.JApplet {

    /**
     * Initializes the applet xlsBudget
     */
    private EITLTableModel DataModelBudget = new EITLTableModel();
    private EITLTableModel DataModelNonBudget = new EITLTableModel();
    private EITLTableModel DataModelUnderApprovalBudget = new EITLTableModel();
    public EITLERP.FeltSales.Budget.clsxlsExcelExporter exprt = new EITLERP.FeltSales.Budget.clsxlsExcelExporter();

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

        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int scrwidth = gd.getDisplayMode().getWidth();
        int scrheight = gd.getDisplayMode().getHeight();
        setSize(scrwidth, scrheight);
        file1.setVisible(false);
    }

    /**
     * This method is called from within the init() method to initialize the
     * form. WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        txtyear = new javax.swing.JTextField();
        BudgetTab = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TableB = new javax.swing.JTable();
        bview = new javax.swing.JButton();
        brpt = new javax.swing.JButton();
        bexcel = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        TableNB = new javax.swing.JTable();
        nonbview = new javax.swing.JButton();
        nbrpt = new javax.swing.JButton();
        excelnb = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        TableUA = new javax.swing.JTable();
        uaview = new javax.swing.JButton();
        uarpt = new javax.swing.JButton();
        uaexcel = new javax.swing.JButton();
        file1 = new javax.swing.JFileChooser();
        lblyearto = new javax.swing.JLabel();

        jLabel1.setText("YEAR :");

        txtyear.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtyearFocusLost(evt);
            }
        });

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
        jScrollPane1.setViewportView(TableB);

        bview.setText("View");
        bview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bviewActionPerformed(evt);
            }
        });

        brpt.setText("Report");
        brpt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                brptActionPerformed(evt);
            }
        });

        bexcel.setText("Excel");
        bexcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bexcelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 678, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(bview, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(68, 68, 68)
                .addComponent(brpt, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(70, 70, 70)
                .addComponent(bexcel, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bview, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(brpt, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bexcel, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 363, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        BudgetTab.addTab("Budget", jPanel1);

        TableNB.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(TableNB);

        nonbview.setText("View");
        nonbview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nonbviewActionPerformed(evt);
            }
        });

        nbrpt.setText("Report");
        nbrpt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nbrptActionPerformed(evt);
            }
        });

        excelnb.setText("Excel");
        excelnb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                excelnbActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 678, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(nonbview, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(70, 70, 70)
                .addComponent(nbrpt, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(69, 69, 69)
                .addComponent(excelnb, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nonbview, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nbrpt, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(excelnb, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 354, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        BudgetTab.addTab("Non Budget", jPanel2);

        TableUA.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane3.setViewportView(TableUA);

        uaview.setText("View");
        uaview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                uaviewActionPerformed(evt);
            }
        });

        uarpt.setText("Report");
        uarpt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                uarptActionPerformed(evt);
            }
        });

        uaexcel.setText("Excel");
        uaexcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                uaexcelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 678, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(uaview, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(74, 74, 74)
                .addComponent(uarpt, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(73, 73, 73)
                .addComponent(uaexcel, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(uaview, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(uarpt, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(uaexcel, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 354, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        BudgetTab.addTab("Under Approval", jPanel3);

        file1.setFocusable(false);

        lblyearto.setText("YYYY");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(BudgetTab)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtyear, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(file1, javax.swing.GroupLayout.PREFERRED_SIZE, 346, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblyearto, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtyear, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblyearto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(file1, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(BudgetTab, javax.swing.GroupLayout.PREFERRED_SIZE, 459, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void nonbviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nonbviewActionPerformed
        // TODO add your handling code here:        
        String sql = "SELECT PARTY_CODE,PARTY_NAME,MACHINE_NO,POSITION,POSITION_DESC,STYLE FROM (SELECT PARTY_CODE,PARTY_NAME,MACHINE_NO,MM_MACHINE_POSITION AS POSITION,POSITION_DESC,MM_FELT_STYLE AS STYLE, "
                + "ROUND(MM_FELT_LENGTH*1,2) AS PRESS_LENGTH,ROUND(MM_FELT_WIDTH*1,2) AS PRESS_WIDTH,ROUND(MM_FELT_GSM*1,2) AS PRESS_GSM, "
                + "ROUND(MM_TH_WEIGHT*1,2) AS PRESS_WEIGHT,ROUND(AREA_SQMTR*1,2) AS PRESS_SQMTR,ROUND(MM_FABRIC_LENGTH*1,2) DRY_LENGTH, "
                + "ROUND(MM_FABRIC_WIDTH*1,2) AS DRY_WIDTH,ROUND(MM_SIZE_M2*1,2) AS DRY_SQMTR,ROUND(MM_FABRIC_TH_WEIGHT*1,2) AS DRY_WEIGHT,MM_ITEM_CODE AS QUALITY_NO, "
                + "GROUP_NAME,ROUND(FELT_RATE*1,2) AS SELLING_PRICE,ROUND(COALESCE(DISC_PER,0)*1,2) AS SP_DISCOUNT,ROUND(COALESCE(WIP,0)*1,2) AS WIP, "
                + "ROUND(COALESCE(STOCK,0)*1,2) AS STOCK,'' AS Q1,'' AS Q2,'' AS Q3,'' AS Q4,'BP' AS PARTY_STATUS,COALESCE(PARTY_STATUS,'NEW') AS SYSTEM_STATUS, "
                + "'' AS REMARKS "
                + "FROM "
                + "(SELECT A.PARTY_CODE,PARTY_NAME,A.MACHINE_NO,MM_MACHINE_POSITION,POSITION_DESC,MM_FELT_STYLE,MM_FELT_LENGTH,MM_FELT_WIDTH,MM_FELT_GSM,MM_TH_WEIGHT, "
                + "AREA_SQMTR,MM_FABRIC_LENGTH,MM_FABRIC_WIDTH,MM_SIZE_M2,MM_FABRIC_TH_WEIGHT,MM_ITEM_CODE,GROUP_NAME,FELT_RATE,DISC_PER "
                + "FROM "
                + "( SELECT D.MM_PARTY_CODE AS PARTY_CODE,PARTY_NAME,D.MM_MACHINE_NO AS MACHINE_NO,MM_MACHINE_POSITION,POSITION_DESC,MM_FELT_STYLE,MM_FELT_LENGTH, "
                + "MM_FELT_WIDTH,MM_FELT_GSM,(MM_FELT_LENGTH*MM_FELT_WIDTH*MM_FELT_GSM/1000) AS MM_TH_WEIGHT,MM_FELT_LENGTH*MM_FELT_WIDTH AS AREA_SQMTR,MM_FABRIC_LENGTH, "
                + "MM_FABRIC_WIDTH,MM_SIZE_M2,(MM_FABRIC_LENGTH*MM_FABRIC_WIDTH*MM_FELT_GSM/1000) AS MM_FABRIC_TH_WEIGHT,MM_ITEM_CODE,GROUP_NAME, "
                + "CASE WHEN WT_RATE =0 THEN SQM_CHRG ELSE WT_RATE END  AS FELT_RATE "
                + "FROM PRODUCTION.FELT_MACHINE_MASTER_HEADER H,  PRODUCTION.FELT_MACHINE_MASTER_DETAIL D, PRODUCTION.FELT_QLT_RATE_MASTER R, "
                + "DINESHMILLS.D_SAL_PARTY_MASTER P , PRODUCTION.FELT_MACHINE_POSITION_MST MP "
                + "WHERE H.MM_DOC_NO=D.MM_DOC_NO AND H.APPROVED=1 AND H.CANCELED=0  AND MM_ITEM_CODE = PRODUCT_CODE  AND EFFECTIVE_TO ='0000-00-00'  AND "
                + "P.PARTY_CODE = H.MM_PARTY_CODE AND POSITION_NO = MM_MACHINE_POSITION )  AS A "
                + "LEFT JOIN ( SELECT * FROM  (SELECT PARTY_CODE,PRODUCT_CODE,DISC_PER,EFFECTIVE_FROM,EFFECTIVE_TO, "
                + "CASE WHEN EFFECTIVE_TO IS NULL THEN COALESCE(EFFECTIVE_TO,CURDATE()) WHEN EFFECTIVE_TO ='0000-00-00' THEN COALESCE(EFFECTIVE_TO,CURDATE()) "
                + "ELSE EFFECTIVE_TO END AS EFFECTIVE_TILL_DATE  FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL ) AS RDM "
                + "WHERE CURDATE() BETWEEN EFFECTIVE_FROM AND EFFECTIVE_TILL_DATE ) AS B ON A.MM_ITEM_CODE=B.PRODUCT_CODE AND A.PARTY_CODE=B.PARTY_CODE) AS MM "
                + "LEFT JOIN  (SELECT PR_PARTY_CODE,PR_MACHINE_NO,PR_POSITION_NO,PR_PRODUCT_CODE, "
                + "SUM(COALESCE(CASE WHEN PR_PIECE_STAGE IN ('WEAVING','MENDING','NEEDLING','FONISHING') THEN 1 END,0)) AS WIP, "
                + "SUM(COALESCE(CASE WHEN PR_PIECE_STAGE IN ('IN STOCK','BSR') THEN 1 END,0)) AS STOCK "
                + "FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE  PR_PRIORITY_HOLD_CAN_FLAG IN (0,1,2,3,4,5) "
                + "GROUP BY PR_PARTY_CODE,PR_MACHINE_NO,PR_POSITION_NO,PR_PRODUCT_CODE) AS PR "
                + "ON MM_ITEM_CODE=PR_PRODUCT_CODE AND PARTY_CODE=PR_PARTY_CODE AND PR_MACHINE_NO = MACHINE_NO AND PR_POSITION_NO = MM_MACHINE_POSITION "
                + "LEFT JOIN (SELECT PARTY_CODE AS B_PARTY_CODE,PARTY_STATUS FROM PRODUCTION.FELT_BUDGET WHERE YEAR_FROM='" + txtyear.getText() + "' ) AS BUDJET "
                + "ON PARTY_CODE=B_PARTY_CODE  WHERE  LEFT(PARTY_CODE,1)='8' "
                + "ORDER BY PARTY_CODE,MM_ITEM_CODE,MACHINE_NO,MM_MACHINE_POSITION,MM_ITEM_CODE) AS DDD "
                + "WHERE SYSTEM_STATUS='NEW'";

        data_generate(sql, DataModelNonBudget, "NonBudget");
    }//GEN-LAST:event_nonbviewActionPerformed

    private void bviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bviewActionPerformed
        // TODO add your handling code here:
        String sql;
        sql = "SELECT PARTY_CODE,PARTY_NAME,MACHINE_NO,POSITION_NO,POSITION_DESC,STYLE, "
                + "Q1,Q1KG,Q1SQMTR,Q1NET_AMOUNT,Q2,Q2KG,Q2SQMTR,Q2NET_AMOUNT,Q3,Q3KG,Q3SQMTR,Q3NET_AMOUNT,Q4,Q4KG,Q4SQMTR,Q4NET_AMOUNT, "
                + "TOTAL,TOTAL_KG,TOTAL_SQMTR,NET_AMOUNT,PARTY_STATUS,REMARKS,CASE WHEN MM_MACHINE_NO IS NULL THEN 'PARTY MACHINE POSTION NOT IN MACHINE MASTER' ELSE '' END AS SYSREMARK FROM PRODUCTION.FELT_BUDGET "
                + "LEFT JOIN (SELECT DISTINCT MM_PARTY_CODE,MM_MACHINE_NO,MM_MACHINE_POSITION,MM_COMBINATION_CODE FROM PRODUCTION.FELT_MACHINE_MASTER_DETAIL) AS A "
                + "ON CONCAT(PARTY_CODE,RIGHT(100+MACHINE_NO,2),RIGHT(100+POSITION_NO,2))=CONCAT(A.MM_PARTY_CODE,A.MM_COMBINATION_CODE)  "
                + "WHERE YEAR_FROM='" + txtyear.getText() + "' "
                + "ORDER BY PARTY_CODE,MACHINE_NO,POSITION_NO";
        data_generate(sql, DataModelBudget, "Budget");
    }//GEN-LAST:event_bviewActionPerformed

    private void uaviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uaviewActionPerformed
        // TODO add your handling code here:
        String sql = "SELECT PARTY_CODE,PARTY_NAME,MACHINE_NO,POSITION_NO,POSITION_DESC,STYLE, "
                + "Q1,Q1KG,Q1SQMTR,Q1NET_AMOUNT,Q2,Q2KG,Q2SQMTR,Q2NET_AMOUNT,Q3,Q3KG,Q3SQMTR,Q3NET_AMOUNT,Q4,Q4KG,Q4SQMTR,Q4NET_AMOUNT, "
                + "TOTAL,TOTAL_KG,TOTAL_SQMTR,NET_AMOUNT,PARTY_STATUS,REMARKS,DOC_NO,DATE_FORMAT(CREATED_DATE,'%d/%m/%Y') AS DOC_DATE,CASE WHEN MM_MACHINE_NO IS NULL THEN 'PARTY MACHINE POSTION NOT IN MACHINE MASTER' ELSE '' END AS SYSREMARK FROM PRODUCTION.FELT_BUDGET_DETAIL "
                + "LEFT JOIN (SELECT DISTINCT MM_PARTY_CODE,MM_MACHINE_NO,MM_MACHINE_POSITION,MM_COMBINATION_CODE FROM PRODUCTION.FELT_MACHINE_MASTER_DETAIL) AS A "
                + " ON CONCAT(PARTY_CODE,RIGHT(100+MACHINE_NO,2),RIGHT(100+POSITION_NO,2))=CONCAT(A.MM_PARTY_CODE,A.MM_COMBINATION_CODE)  "
                + "WHERE YEAR_FROM='" + txtyear.getText() + "' AND DOC_NO NOT IN (SELECT DOC_NO FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE STATUS='F') "
                + "ORDER BY DOC_NO,PARTY_CODE,MACHINE_NO,POSITION_NO";
        data_generate(sql, DataModelUnderApprovalBudget, "UABudget");
    }//GEN-LAST:event_uaviewActionPerformed

    private void excelnbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_excelnbActionPerformed
        // TODO add your handling code here:
        try {
            File file = null;
            file1.setVisible(true);
            int returnVal = file1.showSaveDialog(this);
            if (returnVal == file1.APPROVE_OPTION) {
                file = file1.getSelectedFile();
            }
            file1.setVisible(false);

            exprt.fillData(TableNB, new File(file1.getSelectedFile().toString() + ".xls"), "NonBudget");
            JOptionPane.showMessageDialog(null, "Data saved at "
                    + file.toString() + " successfully ...", "Message",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_excelnbActionPerformed

    private void uaexcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uaexcelActionPerformed
        // TODO add your handling code here:
        try {
            File file = null;
            file1.setVisible(true);
            int returnVal = file1.showSaveDialog(this);
            if (returnVal == file1.APPROVE_OPTION) {
                file = file1.getSelectedFile();
            }
            file1.setVisible(false);

            exprt.fillData(TableUA, new File(file1.getSelectedFile().toString() + ".xls"), "UABudget");
            JOptionPane.showMessageDialog(null, "Data saved at "
                    + file.toString() + " successfully ...", "Message",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_uaexcelActionPerformed

    private void bexcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bexcelActionPerformed
        // TODO add your handling code here:
        try {
            File file = null;
            file1.setVisible(true);
            int returnVal = file1.showSaveDialog(this);
            if (returnVal == file1.APPROVE_OPTION) {
                file = file1.getSelectedFile();
            }
            file1.setVisible(false);

            exprt.fillData(TableB, new File(file1.getSelectedFile().toString() + ".xls"), "Budget");
            JOptionPane.showMessageDialog(null, "Data saved at "
                    + file.toString() + " successfully ...", "Message",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_bexcelActionPerformed

    private void nbrptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nbrptActionPerformed
        // TODO add your handling code here:
        try {
            Connection Conn = EITLERP.data.getConn();
            HashMap parameterMap = new HashMap();

            parameterMap.put("YEAR", txtyear.getText());

            ReportRegister rpt = new ReportRegister(parameterMap, Conn);
            String strSQL = "SELECT PARTY_CODE,PARTY_NAME,MACHINE_NO,POSITION,POSITION_DESC,STYLE FROM (SELECT PARTY_CODE,PARTY_NAME,MACHINE_NO,MM_MACHINE_POSITION AS POSITION,POSITION_DESC,MM_FELT_STYLE AS STYLE, "
                    + "ROUND(MM_FELT_LENGTH*1,2) AS PRESS_LENGTH,ROUND(MM_FELT_WIDTH*1,2) AS PRESS_WIDTH,ROUND(MM_FELT_GSM*1,2) AS PRESS_GSM, "
                    + "ROUND(MM_TH_WEIGHT*1,2) AS PRESS_WEIGHT,ROUND(AREA_SQMTR*1,2) AS PRESS_SQMTR,ROUND(MM_FABRIC_LENGTH*1,2) DRY_LENGTH, "
                    + "ROUND(MM_FABRIC_WIDTH*1,2) AS DRY_WIDTH,ROUND(MM_SIZE_M2*1,2) AS DRY_SQMTR,ROUND(MM_FABRIC_TH_WEIGHT*1,2) AS DRY_WEIGHT,MM_ITEM_CODE AS QUALITY_NO, "
                    + "GROUP_NAME,ROUND(FELT_RATE*1,2) AS SELLING_PRICE,ROUND(COALESCE(DISC_PER,0)*1,2) AS SP_DISCOUNT,ROUND(COALESCE(WIP,0)*1,2) AS WIP, "
                    + "ROUND(COALESCE(STOCK,0)*1,2) AS STOCK,'' AS Q1,'' AS Q2,'' AS Q3,'' AS Q4,'BP' AS PARTY_STATUS,COALESCE(PARTY_STATUS,'NEW') AS SYSTEM_STATUS, "
                    + "'' AS REMARKS "
                    + "FROM "
                    + "(SELECT A.PARTY_CODE,PARTY_NAME,A.MACHINE_NO,MM_MACHINE_POSITION,POSITION_DESC,MM_FELT_STYLE,MM_FELT_LENGTH,MM_FELT_WIDTH,MM_FELT_GSM,MM_TH_WEIGHT, "
                    + "AREA_SQMTR,MM_FABRIC_LENGTH,MM_FABRIC_WIDTH,MM_SIZE_M2,MM_FABRIC_TH_WEIGHT,MM_ITEM_CODE,GROUP_NAME,FELT_RATE,DISC_PER "
                    + "FROM "
                    + "( SELECT D.MM_PARTY_CODE AS PARTY_CODE,PARTY_NAME,D.MM_MACHINE_NO AS MACHINE_NO,MM_MACHINE_POSITION,POSITION_DESC,MM_FELT_STYLE,MM_FELT_LENGTH, "
                    + "MM_FELT_WIDTH,MM_FELT_GSM,(MM_FELT_LENGTH*MM_FELT_WIDTH*MM_FELT_GSM/1000) AS MM_TH_WEIGHT,MM_FELT_LENGTH*MM_FELT_WIDTH AS AREA_SQMTR,MM_FABRIC_LENGTH, "
                    + "MM_FABRIC_WIDTH,MM_SIZE_M2,(MM_FABRIC_LENGTH*MM_FABRIC_WIDTH*MM_FELT_GSM/1000) AS MM_FABRIC_TH_WEIGHT,MM_ITEM_CODE,GROUP_NAME, "
                    + "CASE WHEN WT_RATE =0 THEN SQM_CHRG ELSE WT_RATE END  AS FELT_RATE "
                    + "FROM PRODUCTION.FELT_MACHINE_MASTER_HEADER H,  PRODUCTION.FELT_MACHINE_MASTER_DETAIL D, PRODUCTION.FELT_QLT_RATE_MASTER R, "
                    + "DINESHMILLS.D_SAL_PARTY_MASTER P , PRODUCTION.FELT_MACHINE_POSITION_MST MP "
                    + "WHERE H.MM_DOC_NO=D.MM_DOC_NO AND H.APPROVED=1 AND H.CANCELED=0  AND MM_ITEM_CODE = PRODUCT_CODE  AND EFFECTIVE_TO ='0000-00-00'  AND "
                    + "P.PARTY_CODE = H.MM_PARTY_CODE AND POSITION_NO = MM_MACHINE_POSITION )  AS A "
                    + "LEFT JOIN ( SELECT * FROM  (SELECT PARTY_CODE,PRODUCT_CODE,DISC_PER,EFFECTIVE_FROM,EFFECTIVE_TO, "
                    + "CASE WHEN EFFECTIVE_TO IS NULL THEN COALESCE(EFFECTIVE_TO,CURDATE()) WHEN EFFECTIVE_TO ='0000-00-00' THEN COALESCE(EFFECTIVE_TO,CURDATE()) "
                    + "ELSE EFFECTIVE_TO END AS EFFECTIVE_TILL_DATE  FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL ) AS RDM "
                    + "WHERE CURDATE() BETWEEN EFFECTIVE_FROM AND EFFECTIVE_TILL_DATE ) AS B ON A.MM_ITEM_CODE=B.PRODUCT_CODE AND A.PARTY_CODE=B.PARTY_CODE) AS MM "
                    + "LEFT JOIN  (SELECT PR_PARTY_CODE,PR_MACHINE_NO,PR_POSITION_NO,PR_PRODUCT_CODE, "
                    + "SUM(COALESCE(CASE WHEN PR_PIECE_STAGE IN ('WEAVING','MENDING','NEEDLING','FONISHING') THEN 1 END,0)) AS WIP, "
                    + "SUM(COALESCE(CASE WHEN PR_PIECE_STAGE IN ('IN STOCK','BSR') THEN 1 END,0)) AS STOCK "
                    + "FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE  PR_PRIORITY_HOLD_CAN_FLAG IN (0,1,2,3,4,5) "
                    + "GROUP BY PR_PARTY_CODE,PR_MACHINE_NO,PR_POSITION_NO,PR_PRODUCT_CODE) AS PR "
                    + "ON MM_ITEM_CODE=PR_PRODUCT_CODE AND PARTY_CODE=PR_PARTY_CODE AND PR_MACHINE_NO = MACHINE_NO AND PR_POSITION_NO = MM_MACHINE_POSITION "
                    + "LEFT JOIN (SELECT PARTY_CODE AS B_PARTY_CODE,PARTY_STATUS FROM PRODUCTION.FELT_BUDGET WHERE YEAR_FROM='" + txtyear.getText() + "' ) AS BUDJET "
                    + "ON PARTY_CODE=B_PARTY_CODE  WHERE  LEFT(PARTY_CODE,1)='8' "
                    + "ORDER BY PARTY_CODE,MM_ITEM_CODE,MACHINE_NO,MM_MACHINE_POSITION,MM_ITEM_CODE) AS DDD "
                    + "WHERE SYSTEM_STATUS='NEW'";
            rpt.setReportName("/EITLERP/FeltSales/Reports/NonBudget.jrxml", 1, strSQL); //productlist is the name of my jasper file.
            rpt.callReport();
        } catch (Exception e) {

        }

    }//GEN-LAST:event_nbrptActionPerformed

    private void brptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_brptActionPerformed
        // TODO add your handling code here:
        try {
            Connection Conn = EITLERP.data.getConn();
            HashMap parameterMap = new HashMap();

            parameterMap.put("YEAR", txtyear.getText());

            ReportRegister rpt = new ReportRegister(parameterMap, Conn);
            String strSQL = "SELECT PARTY_CODE,PARTY_NAME,MACHINE_NO,POSITION_NO,POSITION_DESC,STYLE, "
                    + "Q1,Q1KG,Q1SQMTR,Q1NET_AMOUNT,Q2,Q2KG,Q2SQMTR,Q2NET_AMOUNT,Q3,Q3KG,Q3SQMTR,Q3NET_AMOUNT,Q4,Q4KG,Q4SQMTR,Q4NET_AMOUNT, "
                    + "TOTAL,TOTAL_KG,TOTAL_SQMTR,NET_AMOUNT,PARTY_STATUS,REMARKS,CASE WHEN MM_MACHINE_NO IS NULL THEN 'PARTY MACHINE POSTION NOT IN MASTER' ELSE '' END AS SYSREMARK FROM PRODUCTION.FELT_BUDGET "
                    + " LEFT JOIN (SELECT DISTINCT MM_PARTY_CODE,MM_MACHINE_NO,MM_MACHINE_POSITION,MM_COMBINATION_CODE FROM PRODUCTION.FELT_MACHINE_MASTER_DETAIL) AS A "
                    + " ON CONCAT(PARTY_CODE,RIGHT(100+MACHINE_NO,2),RIGHT(100+POSITION_NO,2))=CONCAT(A.MM_PARTY_CODE,A.MM_COMBINATION_CODE)  "
                    + "WHERE YEAR_FROM='" + txtyear.getText() + "' "
                    + "ORDER BY PARTY_CODE,MACHINE_NO,POSITION_NO";
            rpt.setReportName("/EITLERP/FeltSales/Reports/Budget1.jrxml", 1, strSQL); //productlist is the name of my jasper file.
            rpt.callReport();
        } catch (Exception e) {

        }
    }//GEN-LAST:event_brptActionPerformed

    private void uarptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uarptActionPerformed
        // TODO add your handling code here:
        try {
            Connection Conn = EITLERP.data.getConn();
            HashMap parameterMap = new HashMap();

            parameterMap.put("YEAR", txtyear.getText());

            ReportRegister rpt = new ReportRegister(parameterMap, Conn);
            String strSQL = "SELECT PARTY_CODE,PARTY_NAME,MACHINE_NO,POSITION_NO,POSITION_DESC,STYLE, "
                    + "Q1,Q1KG,Q1SQMTR,Q1NET_AMOUNT,Q2,Q2KG,Q2SQMTR,Q2NET_AMOUNT,Q3,Q3KG,Q3SQMTR,Q3NET_AMOUNT,Q4,Q4KG,Q4SQMTR,Q4NET_AMOUNT, "
                    + "TOTAL,TOTAL_KG,TOTAL_SQMTR,NET_AMOUNT,PARTY_STATUS,REMARKS,DOC_NO,DATE_FORMAT(CREATED_DATE,'%d/%m/%Y') AS DOC_DATE,,CASE WHEN MM_MACHINE_NO IS NULL THEN 'PARTY MACHINE POSTION NOT IN MASTER' ELSE '' END AS SYSREMARK FROM PRODUCTION.FELT_BUDGET_DETAIL "
                    + " LEFT JOIN (SELECT DISTINCT MM_PARTY_CODE,MM_MACHINE_NO,MM_MACHINE_POSITION,MM_COMBINATION_CODE FROM PRODUCTION.FELT_MACHINE_MASTER_DETAIL) AS A "
                    + " ON CONCAT(PARTY_CODE,MACHINE_NO,POSITION_NO)=CONCAT(A.MM_PARTY_CODE,A.MM_COMBINATION_CODE)  "
                    + "WHERE YEAR_FROM='" + txtyear.getText() + "' AND DOC_NO NOT IN (SELECT DOC_NO FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE STATUS='F') "
                    + "ORDER BY DOC_NO,PARTY_CODE,MACHINE_NO,POSITION_NO";

            rpt.setReportName("/EITLERP/FeltSales/Reports/BudgetUA.jrxml", 1, strSQL); //productlist is the name of my jasper file.
            rpt.callReport();
        } catch (Exception e) {

        }
    }//GEN-LAST:event_uarptActionPerformed

    private void txtyearFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtyearFocusLost
        // TODO add your handling code here:
        try {
            lblyearto.setText(String.valueOf(Integer.parseInt(txtyear.getText()) + 1));
        } catch (Exception e) {
            txtyear.setText("");
            e.printStackTrace();
        }
    }//GEN-LAST:event_txtyearFocusLost


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane BudgetTab;
    private javax.swing.JTable TableB;
    private javax.swing.JTable TableNB;
    private javax.swing.JTable TableUA;
    private javax.swing.JButton bexcel;
    private javax.swing.JButton brpt;
    private javax.swing.JButton bview;
    private javax.swing.JButton excelnb;
    private javax.swing.JFileChooser file1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblyearto;
    private javax.swing.JButton nbrpt;
    private javax.swing.JButton nonbview;
    private javax.swing.JTextField txtyear;
    private javax.swing.JButton uaexcel;
    private javax.swing.JButton uarpt;
    private javax.swing.JButton uaview;
    // End of variables declaration//GEN-END:variables
private void data_generate(String msql, EITLTableModel mTableModel, String mTable) {

        boolean chk = false;
        ResultSet rs;
        if (txtyear.getText().trim().length() >= 4) {
            chk = true;
        } else {
            JOptionPane.showMessageDialog(this, "Please Enter Year...", "ERROR", JOptionPane.ERROR_MESSAGE);
            txtyear.requestFocus();
        }
        if (chk) {
            try {
                mTableModel = new EITLTableModel();
                if (mTable.equalsIgnoreCase("Budget")) {
                    TableB.removeAll();
                    TableB.setModel(mTableModel);
                    TableB.setAutoResizeMode(TableB.AUTO_RESIZE_OFF);
                }
                if (mTable.equalsIgnoreCase("NonBudget")) {
                    TableNB.removeAll();
                    TableNB.setModel(mTableModel);
                    TableNB.setAutoResizeMode(TableNB.AUTO_RESIZE_OFF);
                }
                if (mTable.equalsIgnoreCase("UABudget")) {
                    TableUA.removeAll();
                    TableUA.setModel(mTableModel);
                    TableUA.setAutoResizeMode(TableUA.AUTO_RESIZE_OFF);
                }
                System.out.println("Sql Query :" + msql);
                rs = EITLERP.data.getResult(msql);
                ResultSetMetaData rsInfo = rs.getMetaData();

                //Format the table from the resultset meta data
                int i = 1;
                for (i = 1; i <= rsInfo.getColumnCount(); i++) {
                    mTableModel.addColumn(rsInfo.getColumnName(i));
                }
                rs.first();
                if (rs.getRow() > 0) {
                    while (!rs.isAfterLast()) {
                        Object[] rowData = new Object[100];
                        for (int m = 1; m < i; m++) {
                            rowData[m - 1] = rs.getString(m);
                        }
                        mTableModel.addRow(rowData);
                        rs.next();
                    }
                }
                mTableModel.TableReadOnly(true);
            } catch (Exception s) {
                s.printStackTrace();
            }
        }
    }
}
