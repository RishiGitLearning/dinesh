/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltWH;

import EITLERP.JTextFieldHint;
import EITLERP.LOV;
import EITLERP.ReportRegister;
import EITLERP.data;
import java.sql.Connection;
import java.util.HashMap;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author Dharmendra
 */
public class FrmTagCard extends javax.swing.JApplet {

    /**
     * Initializes the applet FrmTagCard
     */
    @Override
    public void init() {
        /* Create and display the applet */
        setSize(500, 300);
        initComponents();
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
        jButton1 = new javax.swing.JButton();
        txtPieceNo = new javax.swing.JTextField();
        txtPieceNo = new JTextFieldHint(new JTextField(),"Search by F1");
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        getContentPane().setLayout(null);

        jLabel1.setText("Piece No.");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(20, 50, 70, 30);

        jButton1.setText("Print");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1);
        jButton1.setBounds(100, 110, 110, 30);

        txtPieceNo.setToolTipText("Press F1 ");
        txtPieceNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPieceNoFocusLost(evt);
            }
        });
        txtPieceNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPieceNoKeyPressed(evt);
            }
        });
        getContentPane().add(txtPieceNo);
        txtPieceNo.setBounds(100, 50, 120, 30);

        jButton2.setText("Print Label For Outside Box");
        jButton2.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton2);
        jButton2.setBounds(100, 210, 210, 30);

        jButton3.setText("Print Label For Inside Box");
        jButton3.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton3);
        jButton3.setBounds(100, 160, 210, 30);
    }// </editor-fold>//GEN-END:initComponents

    private void txtPieceNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPieceNoFocusLost
        // TODO add your handling code here:
        String SQL = "";
        SQL = "SELECT PI_PIECE_NO FROM PRODUCTION.FELT_POST_INVOICE_DATA WHERE PI_PIECE_NO LIKE '%" + txtPieceNo.getText() + "%'";

        if (txtPieceNo.getText().equalsIgnoreCase("")) {

        } else if (data.getStringValueFromDB(SQL).equalsIgnoreCase("")) {
            JOptionPane.showMessageDialog(this, "Invalid Piece No.", "ERROR", JOptionPane.ERROR_MESSAGE);
            txtPieceNo.setText("");
            txtPieceNo.requestFocus();
        }
    }//GEN-LAST:event_txtPieceNoFocusLost

    private void txtPieceNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPieceNoKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            LOV aList = new LOV();

            //aList.SQL="SELECT PARTY_CODE,NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER ORDER BY NAME";
            aList.SQL = "SELECT DISTINCT PI_PIECE_NO FROM PRODUCTION.FELT_POST_INVOICE_DATA ";

            aList.ReturnCol = 1;
            aList.ShowReturnCol = true;
            aList.DefaultSearchOn = 1;

            if (aList.ShowLOV()) {
                txtPieceNo.setText(aList.ReturnVal);
            }
        }
    }//GEN-LAST:event_txtPieceNoKeyPressed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        try {
            Connection Conn = EITLERP.data.getConn();
            HashMap parameterMap = new HashMap();

            ReportRegister rpt = new ReportRegister(parameterMap, Conn);
            String strSQL = "SELECT D.PI_PIECE_NO,D.BALE_NO,D.PARTY_NAME,PR_MACHINE_NO,M.POSITION_DESC,"
                    + "FORMAT(PR_BILL_LENGTH,2) AS PR_BILL_LENGTH,"
                    + "FORMAT(PR_BILL_WIDTH,2) AS PR_BILL_WIDTH,PR_SYN_PER,PR_BILL_GSM,FORMAT(PR_BILL_SQMTR,2) AS PR_BILL_SQMTR,"
                    + "PR_BILL_STYLE,FORMAT(PR_BILL_WEIGHT,2) AS PR_BILL_WEIGHT,COALESCE(FORMAT(D.GROSS_WEIGHT,2),'') AS GROSS_WEIGHT,D.DISPATCH_STATION "
                    + " FROM PRODUCTION.FELT_POST_INVOICE_DATA D "
                    + "LEFT JOIN PRODUCTION.FELT_SALES_PIECE_REGISTER ON PI_PIECE_NO=PR_PIECE_NO "
                    + "LEFT JOIN PRODUCTION.FELT_MACHINE_POSITION_MST M ON PR_POSITION_NO=POSITION_NO "
                    + "LEFT JOIN PRODUCTION.FELT_SAL_INVOICE_HEADER H ON D.INV_NO=H.INVOICE_NO AND D.INV_DATE=H.INVOICE_DATE AND D.BALE_NO=H.BALE_NO "
                    + "LEFT JOIN PRODUCTION.FELT_PKG_SLIP_HEADER P ON H.BALE_NO=P.PKG_BALE_NO AND H.PACKING_DATE=P.PKG_BALE_DATE "
                    + "WHERE PI_PIECE_NO='"+txtPieceNo.getText()+"'";
            rpt.setReportName("/EITLERP/FeltWH/Tag_Card.jrxml", 1, strSQL); //productlist is the name of my jasper file.
            rpt.callReport();
        } catch (Exception e) {

        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
try {
            Connection Conn = EITLERP.data.getConn();
            HashMap parameterMap = new HashMap();
            
            parameterMap.put("CONTEXT","http://200.0.0.227:8080/SDMLERP/");
            
            ReportRegister rpt = new ReportRegister(parameterMap, Conn);
            String strSQL = "SELECT D.PI_PIECE_NO,D.BALE_NO,D.PARTY_NAME,PR_MACHINE_NO,M.POSITION_DESC,"
                    + "FORMAT(PR_BILL_LENGTH,2) AS PR_BILL_LENGTH,"
                    + "FORMAT(PR_BILL_WIDTH,2) AS PR_BILL_WIDTH,PR_SYN_PER,PR_BILL_GSM,FORMAT(PR_BILL_SQMTR,2) AS PR_BILL_SQMTR,"
                    + "PR_BILL_STYLE,FORMAT(PR_BILL_WEIGHT,2) AS PR_BILL_WEIGHT,COALESCE(FORMAT(D.GROSS_WEIGHT,2),'') AS GROSS_WEIGHT,D.DISPATCH_STATION "
                    + " FROM PRODUCTION.FELT_POST_INVOICE_DATA D "
                    + "LEFT JOIN PRODUCTION.FELT_SALES_PIECE_REGISTER ON PI_PIECE_NO=PR_PIECE_NO "
                    + "LEFT JOIN PRODUCTION.FELT_MACHINE_POSITION_MST M ON PR_POSITION_NO=POSITION_NO "
                    + "LEFT JOIN PRODUCTION.FELT_SAL_INVOICE_HEADER H ON D.INV_NO=H.INVOICE_NO AND D.INV_DATE=H.INVOICE_DATE AND D.BALE_NO=H.BALE_NO "
                    + "LEFT JOIN PRODUCTION.FELT_PKG_SLIP_HEADER P ON H.BALE_NO=P.PKG_BALE_NO AND H.PACKING_DATE=P.PKG_BALE_DATE "
                    + "WHERE PI_PIECE_NO='"+txtPieceNo.getText()+"'";
            rpt.setReportName("/EITLERP/FeltWH/Label_For_Outside_Packing_Box.jrxml", 1, strSQL); //productlist is the name of my jasper file.
            rpt.callReport();
        } catch (Exception e) {

        }        // TODO add your handling code here:        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
try {
            Connection Conn = EITLERP.data.getConn();
            HashMap parameterMap = new HashMap();
            parameterMap.put("CONTEXT","http://200.0.0.227:8080/SDMLERP/");

            ReportRegister rpt = new ReportRegister(parameterMap, Conn);
            String strSQL = "SELECT D.PI_PIECE_NO,D.BALE_NO,D.PARTY_NAME,PR_MACHINE_NO,M.POSITION_DESC,"
                    + "FORMAT(PR_BILL_LENGTH,2) AS PR_BILL_LENGTH,"
                    + "FORMAT(PR_BILL_WIDTH,2) AS PR_BILL_WIDTH,PR_SYN_PER,PR_BILL_GSM,FORMAT(PR_BILL_SQMTR,2) AS PR_BILL_SQMTR,"
                    + "PR_BILL_STYLE,FORMAT(PR_BILL_WEIGHT,2) AS PR_BILL_WEIGHT,COALESCE(FORMAT(D.GROSS_WEIGHT,2),'') AS GROSS_WEIGHT,D.DISPATCH_STATION "
                    + " FROM PRODUCTION.FELT_POST_INVOICE_DATA D "
                    + "LEFT JOIN PRODUCTION.FELT_SALES_PIECE_REGISTER ON PI_PIECE_NO=PR_PIECE_NO "
                    + "LEFT JOIN PRODUCTION.FELT_MACHINE_POSITION_MST M ON PR_POSITION_NO=POSITION_NO "
                    + "LEFT JOIN PRODUCTION.FELT_SAL_INVOICE_HEADER H ON D.INV_NO=H.INVOICE_NO AND D.INV_DATE=H.INVOICE_DATE AND D.BALE_NO=H.BALE_NO "
                    + "LEFT JOIN PRODUCTION.FELT_PKG_SLIP_HEADER P ON H.BALE_NO=P.PKG_BALE_NO AND H.PACKING_DATE=P.PKG_BALE_DATE "
                    + "WHERE PI_PIECE_NO='"+txtPieceNo.getText()+"'";
            rpt.setReportName("/EITLERP/FeltWH/Label_For_Inside_Packing_Box.jrxml", 1, strSQL); //productlist is the name of my jasper file.
            rpt.callReport();
        } catch (Exception e) {

        }        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTextField txtPieceNo;
    // End of variables declaration//GEN-END:variables
}
