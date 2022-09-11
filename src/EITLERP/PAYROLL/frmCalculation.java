/*
 * frmChangePassword.java
 *
 * Created on July 3, 2004, 3:36 PM
 */
package EITLERP.PAYROLL;

import EITLERP.*;
import java.awt.Color;
import java.util.Date;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.table.TableColumn;

/*<APPLET CODE=frmChangePassword HEIGHT=200 WIDTH=430></APPLET>*/
/**
 *
 * @author Daxesh Prajapati
 */
public class frmCalculation extends javax.swing.JApplet {

    private EITLTableModel DataModel;
    /**
     * Initializes the applet frmChangePassword
     */
    public void init() {
        initComponents();
        setSize(1085, 680);
        FormatGrid();
        lblTitle.setForeground(Color.WHITE);
    }
    
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitle = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        Table = new javax.swing.JTable();

        getContentPane().setLayout(null);

        lblTitle.setBackground(new java.awt.Color(0, 102, 153));
        lblTitle.setText("PAYROLL PROCESS");
        lblTitle.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblTitle.setOpaque(true);
        getContentPane().add(lblTitle);
        lblTitle.setBounds(0, 2, 1080, 25);

        jButton1.setText("Load Data");
        getContentPane().add(jButton1);
        jButton1.setBounds(20, 40, 130, 29);

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
        jScrollPane1.setBounds(20, 80, 1040, 260);
    }// </editor-fold>//GEN-END:initComponents
  private void FormatGrid() {  
  try {
            DataModel = new EITLTableModel();
            Table.removeAll();

            Table.setModel(DataModel);
            Table.setAutoResizeMode(0);

            DataModel.addColumn("SrNo"); //0 - Read Only
            DataModel.addColumn("MACHINE NO"); //1
            DataModel.addColumn("POSITION"); //2
            DataModel.addColumn("POSITION DESC"); //3
            DataModel.addColumn("PIECE NO"); //4
            DataModel.addColumn("PRODUCT"); //5
            DataModel.addColumn("DESCRIPTION"); //6
            DataModel.addColumn("GROUP"); //7
            DataModel.addColumn("LENGTH"); //8
            DataModel.addColumn("WIDTH"); //9
            DataModel.addColumn("GSM"); //10
            DataModel.addColumn("THORTICAL WEIGHT"); //11
            DataModel.addColumn("SQ MT"); //12
            DataModel.addColumn("STYLE"); //13
            
            

            DataModel.SetVariable(0, "SrNo"); //0 - Read Only
            DataModel.SetVariable(1, "MACHINE_NO"); //1
            DataModel.SetVariable(2, "POSITION"); //2
            DataModel.SetVariable(3, "POSITION_DESC"); //3
            DataModel.SetVariable(4, "PIECE_NO"); //4
            DataModel.SetVariable(5, "PRODUCT"); //5
            DataModel.SetVariable(6, "DESCRIPTION"); //6
            DataModel.SetVariable(7, "GROUP"); //7
            DataModel.SetVariable(8, "LENGTH"); //8
            DataModel.SetVariable(9, "WIDTH"); //9
            DataModel.SetVariable(10, "GSM"); //10
            DataModel.SetVariable(11, "THORTICAL_WIDTH"); //11
            DataModel.SetVariable(12, "SQ_MTR"); //12
            DataModel.SetVariable(13, "STYLE"); //13
            
            Table.getColumnModel().getColumn(0).setMinWidth(20);
            Table.getColumnModel().getColumn(1).setMinWidth(90);
            Table.getColumnModel().getColumn(2).setMinWidth(70);
            Table.getColumnModel().getColumn(3).setMinWidth(120);
            Table.getColumnModel().getColumn(4).setMinWidth(70);
            Table.getColumnModel().getColumn(5).setMinWidth(100);
            Table.getColumnModel().getColumn(6).setMinWidth(120);
            Table.getColumnModel().getColumn(7).setMinWidth(80);
            Table.getColumnModel().getColumn(8).setMinWidth(70);
            Table.getColumnModel().getColumn(9).setMinWidth(70);
            Table.getColumnModel().getColumn(10).setMinWidth(50);
            Table.getColumnModel().getColumn(11).setMinWidth(130);
            Table.getColumnModel().getColumn(12).setMinWidth(80);
            Table.getColumnModel().getColumn(13).setMinWidth(80);
           
            //dateColumn.setCellEditor(new DatePi);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Table;
    private javax.swing.JButton jButton1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblTitle;
    // End of variables declaration//GEN-END:variables
}