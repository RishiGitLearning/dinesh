/*
 * frmFindFeltRate.java
 * This form is used for searching  the details of Felt Rate Master and
 * Felt Rate Updation Modules 
 * Created on July 19, 2013, 5:17 PM
 */

package EITLERP.FeltSales.SalesFollowup;
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


public class frmPieceClubbingDetails_Selection extends javax.swing.JApplet {
    
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
            DataModel.addColumn("CLUBBING");
            DataModel.addColumn("Piece No");
            DataModel.addColumn("OC Month");
            DataModel.addColumn("Curr Sch Month");
            DataModel.addColumn("Piece Stage");
            DataModel.addColumn("Finishing Date");
            DataModel.addColumn("Warehouse Days");
            DataModel.addColumn("Machine NO");
            DataModel.addColumn("Position");
            DataModel.addColumn("Length");
            DataModel.addColumn("Width");
            DataModel.addColumn("GSM");
            DataModel.addColumn("Product Code");

            DataModel.SetVariable(0, "SrNo"); //0 - Read Only
            DataModel.SetVariable(1, "CLUBBING");
            DataModel.SetVariable(2, "PIECE_NO");
            DataModel.SetVariable(3, "OC_MONTH");
            DataModel.SetVariable(4, "CURRENT_SCH_MONTH");
            DataModel.SetVariable(5, "PIECE_STAGE");
            DataModel.SetVariable(6, "FINISHING_DATE");
            DataModel.SetVariable(7, "WH_DAYS");
            DataModel.SetVariable(8, "MACHINE_NO");
            DataModel.SetVariable(9, "POSITION");
            DataModel.SetVariable(10, "LENGTH");
            DataModel.SetVariable(11, "WIDTH");
            DataModel.SetVariable(12, "GSM");
            DataModel.SetVariable(13, "PRODUCT_CODE");

            tblReport.getColumnModel().getColumn(0).setMaxWidth(40);
            tblReport.getColumnModel().getColumn(1).setMinWidth(100);
            tblReport.getColumnModel().getColumn(2).setMinWidth(80);
            tblReport.getColumnModel().getColumn(3).setMinWidth(100);
            tblReport.getColumnModel().getColumn(4).setMinWidth(120);
            tblReport.getColumnModel().getColumn(5).setMinWidth(100);
            tblReport.getColumnModel().getColumn(6).setMinWidth(100);
            tblReport.getColumnModel().getColumn(7).setMinWidth(120);
            tblReport.getColumnModel().getColumn(8).setMinWidth(80);
            tblReport.getColumnModel().getColumn(9).setMinWidth(150);
            tblReport.getColumnModel().getColumn(10).setMinWidth(80);
            tblReport.getColumnModel().getColumn(11).setMinWidth(80);
            tblReport.getColumnModel().getColumn(12).setMinWidth(80);
            tblReport.getColumnModel().getColumn(13).setMinWidth(100);

            DataModel.SetReadOnly(0);
            //DataModel_cl_piece.SetReadOnly(1);
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
            DataModel.SetReadOnly(12);
            DataModel.SetReadOnly(13);

            int ImportCol = 1;
            Renderer1.setCustomComponent(ImportCol, "CheckBox");
            JCheckBox aCheckBox = new JCheckBox();
            aCheckBox.setBackground(Color.WHITE);
            aCheckBox.setVisible(true);
            aCheckBox.setSelected(false);
            tblReport.getColumnModel().getColumn(ImportCol).setCellEditor(new DefaultCellEditor(aCheckBox));
            tblReport.getColumnModel().getColumn(ImportCol).setCellRenderer(Renderer1);
            
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
    
    public void GenerateData(String PartyCode)
    {
         try{
            FormatGrid();
            //ResultSet rsData = data.getResult("SELECT D.*,H.APPROVED_DATE FROM PRODUCTION.SPILLOVER_RESCHEDULING_DETAIL D,PRODUCTION.SPILLOVER_RESCHEDULING_HEADER H   where D.PIECE_NO='"+PieceNo+"' AND D.DOC_NO=H.DOC_NO GROUP BY D.PIECE_NO,D.RE_SCH_MONTH,D.DATE_OF_COMMUNICATION,D.MODE_OF_COMMUNICATION,D.CONTACT_PERSON,D.PARTY_JUSTIFICATION,D.AREA_MANAGER_COMMENT ORDER BY D.PIECE_NO,D.DOC_NO");
            
            ResultSet rsData = data.getResult("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PARTY_CODE='" + PartyCode + "' "
                    + "AND PR_PIECE_STAGE IN ('IN STOCK','SEAMING','NEEDLING','MENDING','FINISHING','OSG STOCK','WEAVING','PLANNING','BSR','SPIRALLING','ASSEMBLY','HEAT_SETTING','MARKING','SPLICING') AND (PR_DELINK!='OBSOLETE' OR PR_DELINK IS NULL) AND PR_PIECE_NO NOT IN (SELECT D.PIECE_NO FROM  PRODUCTION.FELT_SALES_PIECE_CLUBBING_HEADER H, " +
                        "PRODUCTION.FELT_SALES_PIECE_CLUBBING_DETAIL D WHERE H.PARTY_CODE='" + PartyCode + "' " +
                        "AND H.PC_DOC_NO=D.PC_DOC_NO)");
             
            if(data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PARTY_CODE='" + PartyCode + "' "
                    + "AND PR_PIECE_STAGE IN ('IN STOCK','SEAMING','NEEDLING','MENDING','FINISHING','OSG STOCK','WEAVING','PLANNING','BSR','SPIRALLING','ASSEMBLY','HEAT_SETTING','MARKING','SPLICING') AND (PR_DELINK!='OBSOLETE' OR PR_DELINK IS NULL)  AND PR_PIECE_NO NOT IN (SELECT D.PIECE_NO FROM  PRODUCTION.FELT_SALES_PIECE_CLUBBING_HEADER H, " +
                        "PRODUCTION.FELT_SALES_PIECE_CLUBBING_DETAIL D WHERE H.PARTY_CODE='" + PartyCode + "' " +
                        "AND H.PC_DOC_NO=D.PC_DOC_NO)"))
            {
               rsData.first();
               int i=1;
               while(!rsData.isAfterLast())
               {
                   Object[] rowData = new Object[100];

                        rowData[0] = i;
                        rowData[1] = false;
                        rowData[2] = rsData.getString("PR_PIECE_NO");
                        rowData[3] = rsData.getString("PR_OC_MONTHYEAR");
                        rowData[4] = rsData.getString("PR_CURRENT_SCH_MONTH");
                        rowData[5] = rsData.getString("PR_PIECE_STAGE");
                        rowData[6] = rsData.getString("PR_FNSG_DATE");
                        rowData[7] = data.getStringValueFromDB("SELECT DATEDIFF('" + EITLERPGLOBAL.getCurrentDateDB() + "','" + rsData.getString("PR_FNSG_DATE") + "')");
                        rowData[8] = rsData.getString("PR_MACHINE_NO");
                        rowData[9] = rsData.getString("PR_POSITION_NO")+ " - "+ data.getStringValueFromDB("SELECT POSITION_DESC FROM PRODUCTION.FELT_MACHINE_POSITION_MST WHERE POSITION_NO='" + rsData.getString("PR_POSITION_NO") + "'");
                        // + " - " + 
                        rowData[10] = rsData.getString("PR_BILL_LENGTH");
                        rowData[11] = rsData.getString("PR_BILL_WIDTH");
                        rowData[12] = rsData.getString("PR_BILL_GSM");
                        rowData[13] = rsData.getString("PR_BILL_PRODUCT_CODE");
                        
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
        txtPartyCode = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        lblPartyName = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblReport = new javax.swing.JTable();
        btnSave = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        txtRemark = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();

        getContentPane().setLayout(null);

        jLabel1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel1.setText("Select Piece for Clubbing");
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
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPartyCodeKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPartyCodeKeyTyped(evt);
            }
        });
        jPanel1.add(txtPartyCode);
        txtPartyCode.setBounds(120, 10, 90, 30);

        jLabel9.setText("Party Code");
        jPanel1.add(jLabel9);
        jLabel9.setBounds(20, 10, 100, 30);
        jPanel1.add(lblPartyName);
        lblPartyName.setBounds(210, 10, 320, 30);

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
        jScrollPane1.setBounds(10, 70, 920, 310);

        btnSave.setText("SAVE");
        btnSave.setEnabled(false);
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        jPanel1.add(btnSave);
        btnSave.setBounds(800, 40, 130, 28);

        jLabel7.setText("Remark");
        jPanel1.add(jLabel7);
        jLabel7.setBounds(440, 50, 70, 16);
        jPanel1.add(txtRemark);
        txtRemark.setBounds(510, 50, 280, 20);

        jLabel2.setText("WIP - STOCK Pieces ");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(10, 50, 200, 16);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(10, 30, 940, 390);
    }// </editor-fold>//GEN-END:initComponents
        
    private void cmdCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCancelActionPerformed
        stringFindQuery="";
        Cancelled=true;
        getParent().getParent().getParent().getParent().show(false);
    }//GEN-LAST:event_cmdCancelActionPerformed
    
    public void FindByPartyCode(String PartyCode,boolean EditRight)
    {
        btnSave.setEnabled(EditRight);
        
        try{
            ResultSet rs = data.getResult("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PARTY_CODE='" + PartyCode + "' "
                    + "AND PR_PIECE_STAGE IN ('IN STOCK','SEAMING','NEEDLING','MENDING','FINISHING','OSG STOCK','WEAVING','PLANNING','BSR','SPIRALLING','ASSEMBLY','HEAT_SETTING','MARKING','SPLICING') AND (PR_DELINK!='OBSOLETE' OR PR_DELINK IS NULL)");
            //System.out.println("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO = '"+PieceNo+"'");
            if(data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PARTY_CODE='" + PartyCode + "' "
                    + "AND PR_PIECE_STAGE IN ('IN STOCK','SEAMING','NEEDLING','MENDING','FINISHING','OSG STOCK','WEAVING','PLANNING','BSR','SPIRALLING','ASSEMBLY','HEAT_SETTING','MARKING','SPLICING') AND (PR_DELINK!='OBSOLETE' OR PR_DELINK IS NULL)"))
            {
                rs.first();
                
                txtPartyCode.setText(PartyCode);
                lblPartyName.setText(clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, PartyCode));
                FormatGrid();
                GenerateData(PartyCode);
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

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        for (int i = 0; i < tblReport.getRowCount(); i++) {
            if (tblReport.getValueAt(i, 1).equals(true)) {
                String PieceNo = DataModel.getValueByVariable("PIECE_NO", i);

                String PartyCode = data.getStringValueFromDB("SELECT PR_PARTY_CODE FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + PieceNo + "'");
                String REQ_MONTH = data.getStringValueFromDB("SELECT PR_REQUESTED_MONTH FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + PieceNo + "'");
                String OC_MONTH = data.getStringValueFromDB("SELECT PR_OC_MONTHYEAR FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + PieceNo + "'");
                String CURRENT_SALES_PLAN = data.getStringValueFromDB("SELECT PR_CURRENT_SCH_MONTH FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + PieceNo + "'");
                String PIECE_STAGE = data.getStringValueFromDB("SELECT PR_PIECE_STAGE FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + PieceNo + "'");
                
                String DocNo = "PC" + PartyCode + "-" + EITLERPGLOBAL.getCurrentDateDB();
                
                if(!data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_CLUBBING_HEADER WHERE PC_DOC_NO='"+DocNo+"'"))
                {
                    
                    data.Execute(" INSERT INTO PRODUCTION.FELT_SALES_PIECE_CLUBBING_HEADER "
                        + "(PC_DOC_NO, PC_DOC_DATE, REMARK, PARTY_CODE, LAST_OC_MONTH, LAST_CURR_SCH_MONTH, HIERARCHY_ID, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE, APPROVED, APPROVED_BY, APPROVED_DATE, REJECTED, REJECTED_BY, REJECTED_DATE, CANCELED, CHANGED, CHANGED_DATE, REJECTED_REMARKS) values "
                        + "('"+DocNo+"', '"+EITLERPGLOBAL.getCurrentDateDB()+"', '', '"+PartyCode+"', '', '', '4840', '28', '"+EITLERPGLOBAL.getCurrentDateDB()+"', '0', '0000-00-00', 0, 0, '0000-00-00', 0, 0, '0000-00-00', 0, 0, '0000-00-00', '')");
                    data.Execute("INSERT INTO PRODUCTION.FELT_PROD_DOC_DATA"
                                        + " (MODULE_ID, DOC_NO, DOC_DATE, USER_ID, STATUS, TYPE, REMARKS, SR_NO, FROM_USER_ID, FROM_REMARKS, RECEIVED_DATE, ACTION_DATE, CHANGED, CHANGED_DATE) "
                                        + " VALUES "
                                        + " ('870', '" + DocNo + "', '" + EITLERPGLOBAL.getCurrentDateDB() + "', '28', 'W', 'F', '', '1', '28', '', '0000-00-00', '0000-00-00', 0, '0000-00-00')");
                               
                }
                
                System.out.println(" INSERT INTO  PRODUCTION.FELT_SALES_PIECE_CLUBBING_DETAIL "
                        + "(PC_DOC_NO, PC_DOC_DATE, PIECE_NO, REMARK, CURRENT_STATUS, USER_ID,REQ_MONTH,OC_MONTH,CURRENT_SALES_PLAN,PIECE_STAGE) values "
                        + "('" + DocNo + "', '" + EITLERPGLOBAL.getCurrentDateDB() + "', '" + PieceNo + "', '" + txtRemark.getText() + "',  'Active', '" + EITLERPGLOBAL.gUserID + "','"+REQ_MONTH+"','"+OC_MONTH+"','"+CURRENT_SALES_PLAN+"','"+PIECE_STAGE+"')");
                data.Execute(" INSERT INTO  PRODUCTION.FELT_SALES_PIECE_CLUBBING_DETAIL "
                        + "(PC_DOC_NO, PC_DOC_DATE, PIECE_NO, REMARK, CURRENT_STATUS, USER_ID,REQ_MONTH,OC_MONTH,CURRENT_SALES_PLAN,PIECE_STAGE) values "
                        + "('" + DocNo + "', '" + EITLERPGLOBAL.getCurrentDateDB() + "', '" + PieceNo + "', '" + txtRemark.getText() + "',  'Active', '" + EITLERPGLOBAL.gUserID + "','"+REQ_MONTH+"','"+OC_MONTH+"','"+CURRENT_SALES_PLAN+"','"+PIECE_STAGE+"')");
                //PC_DOC_NO, PC_DOC_DATE, PIECE_NO, REMARK, EXPECTED_DISPATCH_DATE, CURRENT_STATUS, USER_ID, ENTRY_DATE
                //lblClubbingNumber.setText(DocNo);
                
                try{
                    data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_CLUBBING_HEADER H,\n" +
                        "(SELECT * FROM (SELECT PC_DOC_NO,PR_CURRENT_SCH_MONTH,PR_OC_MONTHYEAR,PR_CURRENT_SCH_LAST_DDMMYY\n" +
                        "FROM PRODUCTION.FELT_SALES_PIECE_CLUBBING_DETAIL D,PRODUCTION.FELT_SALES_PIECE_REGISTER R\n" +
                        "WHERE D.PIECE_NO=R.PR_PIECE_NO\n" +
                        "ORDER BY PR_CURRENT_SCH_LAST_DDMMYY DESC) AS D\n" +
                        "GROUP BY PC_DOC_NO) AS D\n" +
                        "SET H.LAST_OC_MONTH=PR_OC_MONTHYEAR,H.LAST_CURR_SCH_MONTH=D.PR_CURRENT_SCH_MONTH\n" +
                        "WHERE H.PC_DOC_NO=D.PC_DOC_NO");
                }catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }//GEN-LAST:event_btnSaveActionPerformed
        
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSave;
    private javax.swing.JButton cmdCancel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblPartyName;
    private javax.swing.JTable tblReport;
    private javax.swing.JTextField txtPartyCode;
    private javax.swing.JTextField txtRemark;
    // End of variables declaration//GEN-END:variables
    
}
