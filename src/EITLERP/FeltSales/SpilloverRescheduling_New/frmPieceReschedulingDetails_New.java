/*
 * frmFindFeltRate.java
 * This form is used for searching  the details of Felt Rate Master and
 * Felt Rate Updation Modules 
 * Created on July 19, 2013, 5:17 PM
 */

package EITLERP.FeltSales.SpilloverRescheduling_New;
/**
 *
 * @author  Vivek Kumar
 */


import EITLERP.EITLERPGLOBAL;
import EITLERP.EITLTableCellRenderer;
import EITLERP.EITLTableModel;
import EITLERP.FeltSales.common.LOV;
import EITLERP.clsSales_Party;
import EITLERP.data;
import java.awt.Color;
import java.sql.ResultSet;
import java.util.Date;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.table.TableColumn;


public class frmPieceReschedulingDetails_New extends javax.swing.JApplet {
    
    public boolean Cancelled=true;
    public String stringFindQuery="";   
    public String Doc_No="";
    public boolean Add_New =false;
    private EITLTableModel DataModel;
    private EITLTableCellRenderer Renderer1 = new EITLTableCellRenderer();
    
    public void init() {
        System.gc();
        setSize(950,465);
        initComponents();
        
    }
    
    private void FormatGrid() {
        try {
            DataModel = new EITLTableModel();
            tblReport.removeAll();

            tblReport.setModel(DataModel);
            tblReport.setAutoResizeMode(0);

            DataModel.addColumn("SrNo"); //0 - Read Only
            DataModel.addColumn("Piece no"); //1
            DataModel.addColumn("Follow Up Date"); //1
            DataModel.addColumn("Original OC month"); //1
            DataModel.addColumn("Finishing date"); //1
            DataModel.addColumn("Expected Month of Despatch"); //7
            DataModel.addColumn("Unable to Contact"); //7
            DataModel.addColumn("Date of communication"); //7
            DataModel.addColumn("Mode of communication"); //7
            DataModel.addColumn("Person contacted"); //7
            DataModel.addColumn("Party's justification"); //7
            DataModel.addColumn("Area Manager's comments"); //2

            DataModel.SetVariable(0, "SR_NO"); //0 - Read Only
            DataModel.SetVariable(1, "PIECE_NO"); //1
            DataModel.SetVariable(2, "DATE_OF_RESCHEDULE"); //7
            DataModel.SetVariable(3, "ORIGINAL_OC_MONTH"); //7
            DataModel.SetVariable(4, "FINISHING_DATE"); //7
            DataModel.SetVariable(5, "EXPECTED_MONTH_OF_DISPATCH"); //7
            DataModel.SetVariable(6, "UNABLE_TO_CONTACT"); //7
            DataModel.SetVariable(7, "DATE_OF_COMMUNICATION"); //7
            DataModel.SetVariable(8, "MODE_OF_COMMUNICATION"); //7
            DataModel.SetVariable(9, "CONTACT_PERSON"); //7
            DataModel.SetVariable(10, "PARTY_JUSTIFICATION"); //7
            DataModel.SetVariable(11, "AREA_MANAGER_COMMENT"); //7

            tblReport.getColumnModel().getColumn(0).setMinWidth(20);

            
            tblReport.getColumnModel().getColumn(1).setMinWidth(80);
            tblReport.getColumnModel().getColumn(2).setMinWidth(140);
            tblReport.getColumnModel().getColumn(3).setMinWidth(120);
            tblReport.getColumnModel().getColumn(4).setMinWidth(100);
            tblReport.getColumnModel().getColumn(5).setMinWidth(200);
            tblReport.getColumnModel().getColumn(6).setMinWidth(160);
            tblReport.getColumnModel().getColumn(7).setMinWidth(160);
            tblReport.getColumnModel().getColumn(8).setMinWidth(160);
            tblReport.getColumnModel().getColumn(9).setMinWidth(140);
            tblReport.getColumnModel().getColumn(10).setMinWidth(140);
            tblReport.getColumnModel().getColumn(11).setMinWidth(200);

            int ImportCol1 = 6;
            Renderer1.setCustomComponent(ImportCol1, "CheckBox");
            final JCheckBox aCheckBox1 = new JCheckBox();
            aCheckBox1.setBackground(Color.WHITE);
            aCheckBox1.setVisible(true);
            aCheckBox1.setSelected(false);
            tblReport.getColumnModel().getColumn(ImportCol1).setCellEditor(new DefaultCellEditor(aCheckBox1));
            tblReport.getColumnModel().getColumn(ImportCol1).setCellRenderer(Renderer1);
            
            DataModel.SetReadOnly(0);
            DataModel.SetReadOnly(2);
            DataModel.SetReadOnly(3);
            DataModel.SetReadOnly(4);
            DataModel.SetReadOnly(5);
            DataModel.SetReadOnly(6);
            DataModel.SetReadOnly(7);
            DataModel.SetReadOnly(8);
            DataModel.SetReadOnly(9);
            DataModel.SetReadOnly(10);
            DataModel.SetReadOnly(11);
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void GenerateData(String PieceNo)
    {
         try{
            FormatGrid();
            //ResultSet rsData = data.getResult("SELECT D.*,H.APPROVED_DATE FROM PRODUCTION.SPILLOVER_RESCHEDULING_DETAIL D,PRODUCTION.SPILLOVER_RESCHEDULING_HEADER H   where D.PIECE_NO='"+PieceNo+"' AND D.DOC_NO=H.DOC_NO GROUP BY D.PIECE_NO,D.RE_SCH_MONTH,D.DATE_OF_COMMUNICATION,D.MODE_OF_COMMUNICATION,D.CONTACT_PERSON,D.PARTY_JUSTIFICATION,D.AREA_MANAGER_COMMENT ORDER BY D.PIECE_NO,D.DOC_NO");
            ResultSet rsData = data.getResult("SELECT * FROM PRODUCTION.FELT_SALES_FOLLOWUP_DETAIL_HISTORY where PIECE_NO='"+PieceNo+"'");
             System.out.println("SELECT * FROM PRODUCTION.FELT_SALES_FOLLOWUP_DETAIL_HISTORY where PIECE_NO='"+PieceNo+"'");
            if(data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_SALES_FOLLOWUP_DETAIL_HISTORY where PIECE_NO='"+PieceNo+"'"))
            {
               rsData.first();
               int i=1;
               while(!rsData.isAfterLast())
               {
                   Object[] rowData = new Object[100];

                        rowData[0] = i;
                        rowData[1] = rsData.getString("PIECE_NO");
                        rowData[2] = EITLERPGLOBAL.formatDate(rsData.getString("FOLLOWUP_DATE"));//DATE_OF_RESCHEDULE
                        if("".equals(EITLERPGLOBAL.formatDate(rsData.getString("FOLLOWUP_DATE"))))
                        {
                            rowData[2] = EITLERPGLOBAL.formatDate(rsData.getString("EXPECTED_DISPATCH_DATE"));//DATE_OF_RESCHEDULE
                        }
                        rowData[3] = rsData.getString("OC_MONTH");//ORIGINAL_OC_MONTH
                        rowData[4] = EITLERPGLOBAL.formatDate(rsData.getString("FINISHING_DATE"));//FINISHING_DATE
                        rowData[5] = rsData.getString("EXPECTED_MONTH_OF_DISPATCH");//RESCHEDULED_MONTH
                        
                        if("1".equals(rsData.getString("UNABLE_TO_CONTACT")))
                        {
                            rowData[6] = true;//UNABLE TO CONTACT
                        }
                        else
                        {
                            rowData[6] = false;//UNABLE TO CONTACT
                        }
                        rowData[7] = EITLERPGLOBAL.formatDate(rsData.getString("DATE_OF_COMMUNICATION"));//DATE_OF_COMMUNICATION
                        rowData[8] = rsData.getString("MODE_OF_COMMUNICATION");//MODE_OF_COMMUNICATION
                        rowData[9] = rsData.getString("CONTACT_PERSON");//CONTACT_PERSON
                        rowData[10] = rsData.getString("PARTY_JUSTIFICATION");//PARTY_JUSTIFICATION
                        rowData[11] = rsData.getString("AREA_MANAGER_COMMENT");//AREA_MANAGER_COMMENT

                        i++;
                        DataModel.addRow(rowData);
                        rsData.next();
               }
            }
            
        }catch(Exception  e)
        {
            e.printStackTrace();
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
        cmdCancel = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        txtPieceNo = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtPartyCode = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        lblPartyName = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        txtUPN = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblReport = new javax.swing.JTable();

        getContentPane().setLayout(null);

        jLabel1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel1.setText("Follow Up History");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(10, 7, 310, 15);

        cmdCancel.setText("Cancel");
        cmdCancel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cmdCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCancelActionPerformed(evt);
            }
        });
        getContentPane().add(cmdCancel);
        cmdCancel.setBounds(870, 430, 70, 25);

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

        jLabel19.setText("UPN");
        jPanel1.add(jLabel19);
        jLabel19.setBounds(210, 10, 50, 30);

        txtUPN.setEditable(false);
        jPanel1.add(txtUPN);
        txtUPN.setBounds(260, 10, 160, 30);

        tblReport.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tblReport);

        jPanel1.add(jScrollPane1);
        jScrollPane1.setBounds(10, 80, 920, 300);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(10, 30, 940, 390);
    }// </editor-fold>//GEN-END:initComponents
        
    private void cmdCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCancelActionPerformed
        stringFindQuery="";
        Cancelled=true;
        getParent().getParent().getParent().getParent().show(false);
    }//GEN-LAST:event_cmdCancelActionPerformed
    
    public void FindByPieceNo(String PieceNo)
    {
        try{
            ResultSet rs = data.getResult("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO = '"+PieceNo+"'");
            //System.out.println("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO = '"+PieceNo+"'");
            if(data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO = '"+PieceNo+"'"))
            {
                rs.first();
                txtPieceNo.setText(rs.getString("PR_PIECE_NO"));
                txtUPN.setText(rs.getString("PR_UPN"));
                txtPartyCode.setText(rs.getString("PR_PARTY_CODE"));
                lblPartyName.setText(clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, rs.getString("PR_PARTY_CODE")));
                FormatGrid();
                GenerateData(rs.getString("PR_PIECE_NO"));
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
    private javax.swing.JButton cmdCancel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblPartyName;
    private javax.swing.JTable tblReport;
    private javax.swing.JTextField txtPartyCode;
    private javax.swing.JTextField txtPieceNo;
    private javax.swing.JTextField txtUPN;
    // End of variables declaration//GEN-END:variables
    
}
