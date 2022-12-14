/*
 * frmFindFeltRate.java
 * This form is used for searching  the details of Felt Rate Master and
 * Felt Rate Updation Modules 
 * Created on July 19, 2013, 5:17 PM
 */

package EITLERP.FeltSales.SpilloverRescheduling;
/**
 *
 * @author  Vivek Kumar
 */


import EITLERP.EITLERPGLOBAL;
import EITLERP.FeltSales.common.LOV;
import EITLERP.clsSales_Party;
import EITLERP.data;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.swing.JOptionPane;
import javax.swing.text.MaskFormatter;


public class frmReqMonth_SPCLReqDate extends javax.swing.JApplet {
    
    public boolean Cancelled=true;
    public String stringFindQuery="";   
    public String Doc_No="";
    public boolean Add_New =false;
    public void init() {
        System.gc();
        setSize(510,360);
        initComponents();
        GenerateCombo();
        
    }
    private void GenerateCombo()
    {

        String month_name = "";
        Date date = new Date();
        int month;
        int year = date.getYear() + 1900;

            month = date.getMonth()+1;


        for (int i = 0; i < 10; i++) {
            month = month + 1;

            if (month >= 13) {
                month = 1;
                year = year + 1;
            }

            if (month == 1) {
                month_name = "Jan";
            } else if (month == 2) {
                month_name = "Feb";
            } else if (month == 3) {
                month_name = "Mar";
            } else if (month == 4) {
                month_name = "Apr";
            } else if (month == 5) {
                month_name = "May";
            } else if (month == 6) {
                month_name = "Jun";
            } else if (month == 7) {
                month_name = "Jul";
            } else if (month == 8) {
                month_name = "Aug";
            } else if (month == 9) {
                month_name = "Sep";
            } else if (month == 10) {
                month_name = "Oct";
            } else if (month == 11) {
                month_name = "Nov";
            } else if (month == 12) {
                month_name = "Dec";
            }
            cmbReqMonth.addItem(month_name + " - " + year);
      }

    }
    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        cmdFind = new javax.swing.JButton();
        cmdCancel = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        txtPieceNo = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtPartyCode = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        lblPartyName = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtCurrentOCMonth = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtCurrentReqMonth = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtCurrentOCMonth1 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtCurrentReqMonth1 = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtCurrentOCMonth2 = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtCurrentReqMonth2 = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txtCurrentOCMonth3 = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txtCurrentReqMonth3 = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        txtSpecialReqDate = new javax.swing.JTextField();
        cmbReqMonth = new javax.swing.JComboBox();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        txtUPN = new javax.swing.JTextField();

        getContentPane().setLayout(null);

        jLabel1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel1.setText("Select Req Month / Special Req Date");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(10, 7, 310, 15);

        cmdFind.setMnemonic('F');
        cmdFind.setText("SAVE");
        cmdFind.setToolTipText("");
        cmdFind.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cmdFind.setMargin(new java.awt.Insets(0, 14, 0, 14));
        cmdFind.setNextFocusableComponent(cmdCancel);
        cmdFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdFindActionPerformed(evt);
            }
        });
        getContentPane().add(cmdFind);
        cmdFind.setBounds(350, 290, 70, 25);

        cmdCancel.setText("Cancel");
        cmdCancel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cmdCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCancelActionPerformed(evt);
            }
        });
        getContentPane().add(cmdCancel);
        cmdCancel.setBounds(430, 290, 70, 25);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jPanel1.setLayout(null);

        txtPieceNo.setEditable(false);
        jPanel1.add(txtPieceNo);
        txtPieceNo.setBounds(110, 10, 90, 30);

        jLabel3.setDisplayedMnemonic('G');
        jLabel3.setText("Piece No");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(10, 10, 130, 20);

        txtPartyCode.setEditable(false);
        txtPartyCode.setToolTipText("Press F1 key for search Party Code");
        txtPartyCode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtPartyCodeFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPartyCodeFocusLost(evt);
            }
        });
        txtPartyCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPartyCodeKeyTyped(evt);
            }
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPartyCodeKeyPressed(evt);
            }
        });
        jPanel1.add(txtPartyCode);
        txtPartyCode.setBounds(110, 40, 90, 30);

        jLabel9.setText("Party Code");
        jPanel1.add(jLabel9);
        jLabel9.setBounds(10, 40, 100, 30);
        jPanel1.add(lblPartyName);
        lblPartyName.setBounds(210, 40, 320, 30);

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setLayout(null);

        jLabel2.setText("Current Piece ");
        jPanel2.add(jLabel2);
        jLabel2.setBounds(10, 10, 110, 16);

        jLabel4.setText("OC Month");
        jPanel2.add(jLabel4);
        jLabel4.setBounds(270, 40, 80, 16);

        txtCurrentOCMonth.setEditable(false);
        jPanel2.add(txtCurrentOCMonth);
        txtCurrentOCMonth.setBounds(360, 30, 92, 30);

        jLabel5.setText("Req Month");
        jPanel2.add(jLabel5);
        jLabel5.setBounds(10, 40, 90, 16);

        txtCurrentReqMonth.setEditable(false);
        jPanel2.add(txtCurrentReqMonth);
        txtCurrentReqMonth.setBounds(100, 30, 90, 30);

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel3.setLayout(null);

        jLabel6.setText("Current Piece ");
        jPanel3.add(jLabel6);
        jLabel6.setBounds(10, 10, 110, 16);

        jLabel7.setText("OC Month");
        jPanel3.add(jLabel7);
        jLabel7.setBounds(200, 40, 80, 16);
        jPanel3.add(txtCurrentOCMonth1);
        txtCurrentOCMonth1.setBounds(280, 30, 92, 30);

        jLabel8.setText("Req Month");
        jPanel3.add(jLabel8);
        jLabel8.setBounds(10, 40, 90, 16);
        jPanel3.add(txtCurrentReqMonth1);
        txtCurrentReqMonth1.setBounds(100, 30, 90, 30);

        jPanel2.add(jPanel3);
        jPanel3.setBounds(10, 80, 390, 70);

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel4.setLayout(null);

        jLabel10.setText("Current Piece ");
        jPanel4.add(jLabel10);
        jLabel10.setBounds(10, 10, 110, 16);

        jLabel11.setText("OC Month");
        jPanel4.add(jLabel11);
        jLabel11.setBounds(200, 40, 80, 16);
        jPanel4.add(txtCurrentOCMonth2);
        txtCurrentOCMonth2.setBounds(280, 30, 92, 30);

        jLabel12.setText("Req Month");
        jPanel4.add(jLabel12);
        jLabel12.setBounds(10, 40, 90, 16);
        jPanel4.add(txtCurrentReqMonth2);
        txtCurrentReqMonth2.setBounds(100, 30, 90, 30);

        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel5.setLayout(null);

        jLabel13.setText("Current Piece ");
        jPanel5.add(jLabel13);
        jLabel13.setBounds(10, 10, 110, 16);

        jLabel14.setText("OC Month");
        jPanel5.add(jLabel14);
        jLabel14.setBounds(200, 40, 80, 16);
        jPanel5.add(txtCurrentOCMonth3);
        txtCurrentOCMonth3.setBounds(280, 30, 92, 30);

        jLabel15.setText("Req Month");
        jPanel5.add(jLabel15);
        jLabel15.setBounds(10, 40, 90, 16);
        jPanel5.add(txtCurrentReqMonth3);
        txtCurrentReqMonth3.setBounds(100, 30, 90, 30);

        jPanel4.add(jPanel5);
        jPanel5.setBounds(10, 80, 390, 70);

        jPanel2.add(jPanel4);
        jPanel4.setBounds(10, 80, 390, 70);

        jPanel1.add(jPanel2);
        jPanel2.setBounds(10, 80, 470, 70);

        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel6.setLayout(null);

        jLabel16.setText("Reschedule of Request Month");
        jPanel6.add(jLabel16);
        jLabel16.setBounds(10, 40, 230, 16);

        jLabel18.setText("Special Req Date");
        jPanel6.add(jLabel18);
        jLabel18.setBounds(240, 60, 120, 16);
        jPanel6.add(txtSpecialReqDate);
        txtSpecialReqDate.setBounds(360, 50, 92, 30);

        cmbReqMonth.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "SELECT" }));
        jPanel6.add(cmbReqMonth);
        cmbReqMonth.setBounds(10, 60, 160, 20);

        jLabel20.setText("(DD/MM/YYYY)");
        jPanel6.add(jLabel20);
        jLabel20.setBounds(360, 30, 100, 16);

        jLabel21.setText("Proposed Detail");
        jPanel6.add(jLabel21);
        jLabel21.setBounds(10, 10, 200, 16);

        jPanel1.add(jPanel6);
        jPanel6.setBounds(10, 150, 470, 90);

        jLabel19.setText("UPN");
        jPanel1.add(jLabel19);
        jLabel19.setBounds(210, 10, 50, 30);

        txtUPN.setEditable(false);
        jPanel1.add(txtUPN);
        txtUPN.setBounds(260, 10, 160, 30);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(10, 30, 490, 250);
    }// </editor-fold>//GEN-END:initComponents
        
    private void cmdCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCancelActionPerformed
        stringFindQuery="";
        Cancelled=true;
        getParent().getParent().getParent().getParent().show(false);
    }//GEN-LAST:event_cmdCancelActionPerformed
    
    private void cmdFindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdFindActionPerformed
        Cancelled=false;
        
        String PieceNo = txtPieceNo.getText().trim();
        String Upn = txtUPN.getText().trim();
        String PartyCode = txtPartyCode.getText().trim();
        String CurrReqMonth = txtCurrentReqMonth.getText().trim();
        String CurrOcMonth = txtCurrentOCMonth.getText().trim();
        String NewReqMonth = cmbReqMonth.getSelectedItem().toString();
        
        if(NewReqMonth.equals("SELECT"))
        {  NewReqMonth = ""; }
        String SpecialReqDate = txtSpecialReqDate.getText().trim();
        
        if(!NewReqMonth.equals("") && !SpecialReqDate.equals(""))
        {
            System.out.println("Last date of Req Month "+NewReqMonth+" is : "+LastDayOfReqMonth(NewReqMonth));
            JOptionPane.showMessageDialog(this, "Req Month & Special Req Date, Both not allowed");
            return;
        }
        if(!Upn.equals("") && !PieceNo.equals("") && !PartyCode.equals(""))
        {
            if(!NewReqMonth.equals("") && CurrOcMonth.equals(""))
            {
                //Req Month change valid
                System.out.println("INSERT INTO PRODUCTION.SPILLOVER_RESCHEDULING_TMP_REQ_MONTH_SPCL_REQ_DATE " +
                            "(DOC_NO, PIECE_NO, UPN, PARTY_CODE, PARTY_NAME, REQ_MONTH, OC_MONTH, UPDATED_REQ_MONTH, SPECIAL_REQ_DATE, ENTRY_DATE, CHANGES) " +
                            "VALUES " +
                            "('"+Doc_No+"', '"+PieceNo+"', '"+Upn+"', '"+PartyCode+"', '"+lblPartyName.getText()+"', '"+CurrReqMonth+"', '"+CurrOcMonth+"', '"+NewReqMonth+"', '0000-00-00', '"+EITLERPGLOBAL.getCurrentDateTimeDB()+"', 'REQ_MONTH')");
                data.Execute("INSERT INTO PRODUCTION.SPILLOVER_RESCHEDULING_TMP_REQ_MONTH_SPCL_REQ_DATE " +
                            "(DOC_NO, PIECE_NO, UPN, PARTY_CODE, PARTY_NAME, REQ_MONTH, OC_MONTH, UPDATED_REQ_MONTH, SPECIAL_REQ_DATE, ENTRY_DATE, CHANGES) " +
                            "VALUES " +
                            "('"+Doc_No+"', '"+PieceNo+"', '"+Upn+"', '"+PartyCode+"', '"+lblPartyName.getText()+"', '"+CurrReqMonth+"', '"+CurrOcMonth+"', '"+NewReqMonth+"', '0000-00-00', '"+EITLERPGLOBAL.getCurrentDateTimeDB()+"', 'REQ_MONTH')");
                System.out.println("OK. Req Month change");
            }
            else if(!NewReqMonth.equals("") && !CurrOcMonth.equals(""))
            {
                System.out.println("Last date of Req Month "+NewReqMonth+" is : "+LastDayOfReqMonth(NewReqMonth));
                JOptionPane.showMessageDialog(this, "When OC Month is confirm, Req Month can not change");
            }
            
            if(!SpecialReqDate.equals("") && !CurrOcMonth.equals(""))
            {
                //Special Req Date is valid
                System.out.println("INSERT INTO PRODUCTION.SPILLOVER_RESCHEDULING_TMP_REQ_MONTH_SPCL_REQ_DATE " +
                            "(DOC_NO, PIECE_NO, UPN, PARTY_CODE, PARTY_NAME, REQ_MONTH, OC_MONTH, UPDATED_REQ_MONTH, SPECIAL_REQ_DATE, ENTRY_DATE, CHANGES) " +
                            "VALUES " +
                            "('"+Doc_No+"', '"+PieceNo+"', '"+Upn+"', '"+PartyCode+"', '"+lblPartyName.getText()+"', '"+CurrReqMonth+"', '"+CurrOcMonth+"', '', '"+EITLERPGLOBAL.formatDateDB(txtSpecialReqDate.getText())+"', '"+EITLERPGLOBAL.getCurrentDateTimeDB()+"', 'SPCL_REQ_DATE')");
                data.Execute("INSERT INTO PRODUCTION.SPILLOVER_RESCHEDULING_TMP_REQ_MONTH_SPCL_REQ_DATE " +
                            "(DOC_NO, PIECE_NO, UPN, PARTY_CODE, PARTY_NAME, REQ_MONTH, OC_MONTH, UPDATED_REQ_MONTH, SPECIAL_REQ_DATE, ENTRY_DATE, CHANGES) " +
                            "VALUES " +
                            "('"+Doc_No+"', '"+PieceNo+"', '"+Upn+"', '"+PartyCode+"', '"+lblPartyName.getText()+"', '"+CurrReqMonth+"', '"+CurrOcMonth+"', '', '"+EITLERPGLOBAL.formatDateDB(txtSpecialReqDate.getText())+"', '"+EITLERPGLOBAL.getCurrentDateTimeDB()+"', 'SPCL_REQ_DATE')");
                System.out.println("OK Special Req Date");
            }
            else if(!SpecialReqDate.equals(""))
            {
                JOptionPane.showMessageDialog(this, "Special Req Date is not allowed, When OC Month is not confirmed");
            }
        }
        else
        {
            JOptionPane.showMessageDialog(this, "Invalid Data");
        }
        
        getParent().getParent().getParent().getParent().show(false);
    }//GEN-LAST:event_cmdFindActionPerformed
    private String LastDayOfReqMonth(String Req_Month) {
        int Year = Integer.parseInt(Req_Month.substring(6));
        int Month = 0;
        if (Req_Month.contains("Jan")) {
            Month = 1;
        } else if (Req_Month.contains("Feb")) {
            Month = 2;
        } else if (Req_Month.contains("Mar")) {
            Month = 3;
        } else if (Req_Month.contains("Apr")) {
            Month = 4;
        } else if (Req_Month.contains("May")) {
            Month = 5;
        } else if (Req_Month.contains("Jun")) {
            Month = 6;
        } else if (Req_Month.contains("Jul")) {
            Month = 7;
        } else if (Req_Month.contains("Aug")) {
            Month = 8;
        } else if (Req_Month.contains("Sep")) {
            Month = 9;
        } else if (Req_Month.contains("Oct")) {
            Month = 10;
        } else if (Req_Month.contains("Nov")) {
            Month = 11;
        } else if (Req_Month.contains("Dec")) {
            Month = 12;
        }

        Calendar cal = new GregorianCalendar(Year, Month, 0);
        Date date = cal.getTime();
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //System.out.println("Date : " + sdf.format(date));
        return sdf.format(date);
    }
    public void FindByPieceNo(String PieceNo)
    {
        try{
            ResultSet rs = data.getResult("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO = '"+PieceNo+"'");
            if(data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO = '"+PieceNo+"'"))
            {
                rs.first();
                txtPieceNo.setText(rs.getString("PR_PIECE_NO"));
                txtUPN.setText(rs.getString("PR_UPN"));
                txtPartyCode.setText(rs.getString("PR_PARTY_CODE"));
                lblPartyName.setText(clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, rs.getString("PR_PARTY_CODE")));
                txtCurrentReqMonth.setText(rs.getString("PR_REQUESTED_MONTH"));
                txtCurrentOCMonth.setText(rs.getString("PR_OC_MONTHYEAR"));
                
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
            
    }
    
    
    private void txtPartyCodeFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPartyCodeFocusGained
        // TODO add your handling code here:
      
    }//GEN-LAST:event_txtPartyCodeFocusGained

    private void txtPartyCodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPartyCodeFocusLost
        
    }//GEN-LAST:event_txtPartyCodeFocusLost

    private void txtPartyCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPartyCodeKeyPressed


        
        if (evt.getKeyCode() == 112) {
            try {
                LOV aList = new LOV();

                aList.SQL = "SELECT PARTY_CODE,PARTY_NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER  WHERE MAIN_ACCOUNT_CODE='210010'";
                aList.ReturnCol = 1;
                aList.ShowReturnCol = true;
                aList.DefaultSearchOn = 2;
                aList.UseSpecifiedConn = true;
                aList.dbURL = EITLERPGLOBAL.DatabaseURL;

                if (aList.ShowLOV()) {
                    txtPartyCode.setText(aList.ReturnVal);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error = " + e.getMessage());
            }
        }
    }//GEN-LAST:event_txtPartyCodeKeyPressed

    private void txtPartyCodeKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPartyCodeKeyTyped

    }//GEN-LAST:event_txtPartyCodeKeyTyped
        
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cmbReqMonth;
    private javax.swing.JButton cmdCancel;
    private javax.swing.JButton cmdFind;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JLabel lblPartyName;
    private javax.swing.JTextField txtCurrentOCMonth;
    private javax.swing.JTextField txtCurrentOCMonth1;
    private javax.swing.JTextField txtCurrentOCMonth2;
    private javax.swing.JTextField txtCurrentOCMonth3;
    private javax.swing.JTextField txtCurrentReqMonth;
    private javax.swing.JTextField txtCurrentReqMonth1;
    private javax.swing.JTextField txtCurrentReqMonth2;
    private javax.swing.JTextField txtCurrentReqMonth3;
    private javax.swing.JTextField txtPartyCode;
    private javax.swing.JTextField txtPieceNo;
    private javax.swing.JTextField txtSpecialReqDate;
    private javax.swing.JTextField txtUPN;
    // End of variables declaration//GEN-END:variables
    
}
