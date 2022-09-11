/*
 * LOV.java
 *
 * Created on April 7, 2004, 5:11 PM
 */
/*
 Sample Code
 LOV aList=new LOV();
 
 aList.SQL="SELECT COMPANY_ID,COMPANY_NAME FROM D_COM_COMPANY_MASTER ORDER BY COMPANY_ID";
 aList.ReturnCol=1;
 aList.ShowReturnCol=false;
 aList.DefaultSearchOn=2;
 
 if(aList.ShowLOV())
 {
 JOptionPane.showMessageDialog(null,"Selected "+aList.ReturnVal);
 }
 else
 {
 JOptionPane.showMessageDialog(null,"Cancelled");
 }
 */
/*<APPLET CODE=LOV.Class HEIGHT=300 WIDTH=200></APPLT>
 */
package sdml.felt.commonUI;

import java.util.*;
import javax.swing.*;
import java.awt.*;
import javax.swing.table.*;
import java.sql.*;
import java.net.*;
import java.awt.Frame;

/**
 *
 * @author 
 */
public class MsgWindow extends javax.swing.JApplet {

    public String theMessage = "";
    public HashMap MsgBuffer = new HashMap();

    private JDialog aDialog;
    private int MsgPointer = 0;
    private String fromUser = "";
    /**
     * Creates new form LOV
     */
    public MsgWindow() {
        System.gc();
        initComponents();
    }

    public MsgWindow(String pMessage) {
        System.gc();
        initComponents();
        theMessage = pMessage;
    }

    public boolean ShowWindow() {
        try {
            setSize(471, 380);

            Frame f = findParentFrame(this);

            MsgPointer = MsgBuffer.size(); //Last Message
            DisplayMessage();

            aDialog = new JDialog(f, "Server Message", true);

            aDialog.getContentPane().add("Center", this);
            Dimension appletSize = this.getSize();
            aDialog.setSize(appletSize);
            aDialog.setResizable(false);

            //Place it to center of the screen
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            aDialog.setLocation((int) (screenSize.width - appletSize.getWidth()) / 2, (int) (screenSize.height - appletSize.getHeight()) / 2);

            aDialog.setDefaultCloseOperation(javax.swing.JDialog.DISPOSE_ON_CLOSE);
            aDialog.show();

        } catch (Exception e) {
        }
        return true;
    }

    private void DisplayMessage() {

        if (MsgPointer > 0) {

            String theMessage = (String) MsgBuffer.get(Integer.toString(MsgPointer));
            fromUser = theMessage.substring(0, 20);
            theMessage = theMessage.substring(20).trim();
            lblMessageTitle.setText("Message from " + fromUser.trim().toUpperCase());
            lblMsgNo.setText("Message " + MsgPointer + " of " + MsgBuffer.size());
            lblMessage.setText(theMessage);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        lblMessageTitle = new javax.swing.JLabel();
        cmdBack = new javax.swing.JButton();
        cmdNext = new javax.swing.JButton();
        cmdReply = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        lblMessage = new javax.swing.JTextArea();
        cmdReply1 = new javax.swing.JButton();
        lblMsgNo = new javax.swing.JLabel();
        cmdClose = new javax.swing.JButton();

        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSearchKeyPressed(evt);
            }
        });
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });
        getContentPane().setLayout(null);

        jPanel1.setBackground(new java.awt.Color(255, 255, 204));
        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel1.setLayout(null);

        lblMessageTitle.setText("MESSAGE FROM SERVER");
        jPanel1.add(lblMessageTitle);
        lblMessageTitle.setBounds(7, 5, 427, 14);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(5, 7, 445, 24);

        cmdBack.setText("<<");
        cmdBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdBackActionPerformed(evt);
            }
        });
        getContentPane().add(cmdBack);
        cmdBack.setBounds(11, 296, 59, 23);

        cmdNext.setText(">>");
        cmdNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNextActionPerformed(evt);
            }
        });
        getContentPane().add(cmdNext);
        cmdNext.setBounds(77, 296, 59, 23);

        cmdReply.setText("Reply");
        cmdReply.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdReplyActionPerformed(evt);
            }
        });
        getContentPane().add(cmdReply);
        cmdReply.setBounds(146, 296, 88, 23);

        lblMessage.setEditable(false);
        lblMessage.setLineWrap(true);
        lblMessage.setWrapStyleWord(true);
        jScrollPane1.setViewportView(lblMessage);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(8, 45, 444, 213);

        cmdReply1.setText("Clear All");
        cmdReply1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdReply1ActionPerformed(evt);
            }
        });
        getContentPane().add(cmdReply1);
        cmdReply1.setBounds(243, 296, 88, 23);

        lblMsgNo.setText("Message");
        getContentPane().add(lblMsgNo);
        lblMsgNo.setBounds(10, 266, 201, 14);

        cmdClose.setText("Close");
        cmdClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCloseActionPerformed(evt);
            }
        });
        getContentPane().add(cmdClose);
        cmdClose.setBounds(359, 295, 88, 23);
    }// </editor-fold>//GEN-END:initComponents

    private void cmdCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCloseActionPerformed
        // TODO add your handling code here:
        aDialog.dispose();
    }//GEN-LAST:event_cmdCloseActionPerformed

    private void cmdReply1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdReply1ActionPerformed
        // TODO add your handling code here:
        SDMLERPGLOBAL.ObjClient.MsgBuffer.clear();

        System.gc();
        aDialog.dispose();

    }//GEN-LAST:event_cmdReply1ActionPerformed

    private void cmdBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdBackActionPerformed
        // TODO add your handling code here:
        if (MsgPointer > 1) {
            MsgPointer--;
            DisplayMessage();
        }

    }//GEN-LAST:event_cmdBackActionPerformed

    private void cmdNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNextActionPerformed
        // TODO add your handling code here:
        if (MsgPointer < MsgBuffer.size()) {
            MsgPointer++;
            DisplayMessage();
        }
    }//GEN-LAST:event_cmdNextActionPerformed

    private void cmdReplyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdReplyActionPerformed
        // TODO add your handling code here:
        frmClient aForm = new frmClient(fromUser);
        aForm.ShowWindow();
        aDialog.dispose();
    }//GEN-LAST:event_cmdReplyActionPerformed

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_formMouseClicked

    private void txtSearchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyPressed
        // Add your handling code here:
    }//GEN-LAST:event_txtSearchKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdBack;
    private javax.swing.JButton cmdClose;
    private javax.swing.JButton cmdNext;
    private javax.swing.JButton cmdReply;
    private javax.swing.JButton cmdReply1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea lblMessage;
    private javax.swing.JLabel lblMessageTitle;
    private javax.swing.JLabel lblMsgNo;
    // End of variables declaration//GEN-END:variables

    //Generates Table from the Data
    //Recurses through the hierarchy of classes
    //until it finds Frame
    private Frame findParentFrame(JApplet pApplet) {
        Container c = (Container) pApplet;
        while (c != null) {
            if (c instanceof Frame) {
                return (Frame) c;
            }

            c = c.getParent();
        }
        return (Frame) null;
    }

}