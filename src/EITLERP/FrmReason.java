/*
 * frmTemplate.java
 *
 * Created on April 7, 2004, 3:10 PM
 */
 
package EITLERP;

/** 
 *
 * @author  nhpatel
 */
import javax.swing.*;
import java.awt.*;
import EITLERP.*;
import java.text.*;
import java.util.*;
import java.sql.*;

 

public class FrmReason extends javax.swing.JApplet {
    
    private int EditMode=0;
    private clsReason ObjReason;
    
    /** Creates new form frmTemplate */
    public FrmReason() {
        System.gc();
        setSize(600,400);        
        initComponents();
        
        //Now show the Images
        cmdTop.setIcon(EITLERPGLOBAL.getImage("TOP"));
        cmdBack.setIcon(EITLERPGLOBAL.getImage("BACK"));
        cmdNext.setIcon(EITLERPGLOBAL.getImage("NEXT"));
        cmdLast.setIcon(EITLERPGLOBAL.getImage("LAST"));
        cmdNew.setIcon(EITLERPGLOBAL.getImage("NEW"));
        cmdEdit.setIcon(EITLERPGLOBAL.getImage("EDIT"));
        cmdDelete.setIcon(EITLERPGLOBAL.getImage("DELETE"));
        cmdSave.setIcon(EITLERPGLOBAL.getImage("SAVE"));
        cmdCancel.setIcon(EITLERPGLOBAL.getImage("UNDO"));
        cmdFilter.setIcon(EITLERPGLOBAL.getImage("FIND"));
        cmdPreview.setIcon(EITLERPGLOBAL.getImage("PREVIEW"));
        cmdPrint.setIcon(EITLERPGLOBAL.getImage("PRINT"));
        cmdExit.setIcon(EITLERPGLOBAL.getImage("EXIT"));

        ObjReason=new clsReason();
        
        if(ObjReason.LoadData())
        {
            SetFields(false); 
            ObjReason.MoveFirst();
            DisplayData();
            Generate_List();
            SetMenuForRights();
            ShowMessage("Ready ..........");
        }
        else
        {
            JOptionPane.showMessageDialog(null,"Error loading Department data. Error is "+ObjReason.LastError);
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        ToolBar = new javax.swing.JToolBar();
        cmdTop = new javax.swing.JButton();
        cmdBack = new javax.swing.JButton();
        cmdNext = new javax.swing.JButton();
        cmdLast = new javax.swing.JButton();
        cmdNew = new javax.swing.JButton();
        cmdEdit = new javax.swing.JButton();
        cmdDelete = new javax.swing.JButton();
        cmdSave = new javax.swing.JButton();
        cmdCancel = new javax.swing.JButton();
        cmdFilter = new javax.swing.JButton();
        cmdPreview = new javax.swing.JButton();
        cmdPrint = new javax.swing.JButton();
        cmdExit = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        ListLocation = new javax.swing.JList();
        jLabel3 = new javax.swing.JLabel();
        txtReasonID = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtReasonDesc = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        ListReason = new javax.swing.JList();
        lblStatus = new javax.swing.JLabel();

        getContentPane().setLayout(null);

        ToolBar.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        ToolBar.setRollover(true);
        cmdTop.setToolTipText("First Record");
        cmdTop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdTopActionPerformed(evt);
            }
        });

        ToolBar.add(cmdTop);

        cmdBack.setToolTipText("Previous Record");
        cmdBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdBackActionPerformed(evt);
            }
        });

        ToolBar.add(cmdBack);

        cmdNext.setToolTipText("Next Record");
        cmdNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNextActionPerformed(evt);
            }
        });

        ToolBar.add(cmdNext);

        cmdLast.setToolTipText("Last Record");
        cmdLast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdLastActionPerformed(evt);
            }
        });

        ToolBar.add(cmdLast);

        cmdNew.setToolTipText("New Record");
        cmdNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNewActionPerformed(evt);
            }
        });

        ToolBar.add(cmdNew);

        cmdEdit.setToolTipText("Edit");
        ToolBar.add(cmdEdit);

        cmdDelete.setToolTipText("Delete");
        cmdDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdDeleteActionPerformed(evt);
            }
        });

        ToolBar.add(cmdDelete);

        cmdSave.setToolTipText("Save");
        cmdSave.setEnabled(false);
        cmdSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdSaveActionPerformed(evt);
            }
        });

        ToolBar.add(cmdSave);

        cmdCancel.setToolTipText("Cancel");
        cmdCancel.setEnabled(false);
        cmdCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCancelActionPerformed(evt);
            }
        });

        ToolBar.add(cmdCancel);

        cmdFilter.setToolTipText("Find");
        cmdFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdFilterActionPerformed(evt);
            }
        });

        ToolBar.add(cmdFilter);

        cmdPreview.setToolTipText("Preview");
        cmdPreview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdPreviewActionPerformed(evt);
            }
        });

        ToolBar.add(cmdPreview);

        cmdPrint.setToolTipText("Print");
        cmdPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdPrintActionPerformed(evt);
            }
        });

        ToolBar.add(cmdPrint);

        cmdExit.setToolTipText("Exit");
        cmdExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdExitActionPerformed(evt);
            }
        });

        ToolBar.add(cmdExit);

        getContentPane().add(ToolBar);
        ToolBar.setBounds(0, 0, 800, 40);

        jLabel1.setBackground(new java.awt.Color(0, 102, 153));
        jLabel1.setForeground(java.awt.Color.white);
        jLabel1.setText("Reason Master");
        jLabel1.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel1.setOpaque(true);
        getContentPane().add(jLabel1);
        jLabel1.setBounds(0, 40, 570, 25);

        ListLocation.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        ListLocation.setToolTipText("Select Warehouse Name from list");
        getContentPane().add(ListLocation);
        ListLocation.setBounds(0, 0, 0, 0);

        jLabel3.setText("Reason ID");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(210, 140, 90, 15);

        txtReasonID.setEnabled(false);
        getContentPane().add(txtReasonID);
        txtReasonID.setBounds(310, 140, 70, 19);

        jLabel4.setText("Reason Desc");
        getContentPane().add(jLabel4);
        jLabel4.setBounds(210, 170, 100, 20);

        txtReasonDesc.setEnabled(false);
        txtReasonDesc.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtReasonDescFocusGained(evt);
            }
        });

        getContentPane().add(txtReasonDesc);
        txtReasonDesc.setBounds(310, 170, 230, 19);

        ListReason.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        ListReason.setToolTipText("Select Warehouse Name from list");
        ListReason.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                ListReasonFocusGained(evt);
            }
        });

        jScrollPane1.setViewportView(ListReason);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(10, 70, 190, 270);

        lblStatus.setFont(new java.awt.Font("Tahoma", 1, 12));
        lblStatus.setForeground(new java.awt.Color(51, 51, 255));
        lblStatus.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(lblStatus);
        lblStatus.setBounds(0, 350, 570, 22);

    }//GEN-END:initComponents

    private void ListReasonFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_ListReasonFocusGained
        // TODO add your handling code here:
         ShowMessage("Select Reason Desc for all details ..........");
    }//GEN-LAST:event_ListReasonFocusGained

    private void txtReasonDescFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtReasonDescFocusGained
        // TODO add your handling code here:
            ShowMessage("Enter Reason Description ..........");
    }//GEN-LAST:event_txtReasonDescFocusGained

    private void cmdExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdExitActionPerformed
        // TODO add your handling code here:
        ((JFrame)getParent().getParent().getParent().getParent()).dispose();
    }//GEN-LAST:event_cmdExitActionPerformed

    private void cmdPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPrintActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmdPrintActionPerformed

    private void cmdPreviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPreviewActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmdPreviewActionPerformed

    private void cmdFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdFilterActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmdFilterActionPerformed

    private void cmdCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCancelActionPerformed
        // TODO add your handling code here:
        Cancel();
    }//GEN-LAST:event_cmdCancelActionPerformed

    private void cmdSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSaveActionPerformed
        // TODO add your handling code here:
        Save();
    }//GEN-LAST:event_cmdSaveActionPerformed

    private void cmdDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdDeleteActionPerformed
        // TODO add your handling code here:
        Delete();
    }//GEN-LAST:event_cmdDeleteActionPerformed

    private void cmdNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNewActionPerformed
        // TODO add your handling code here:
        Add();
    }//GEN-LAST:event_cmdNewActionPerformed

    private void cmdLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdLastActionPerformed
        // TODO add your handling code here:
        MoveLast();
        //Edit();
    }//GEN-LAST:event_cmdLastActionPerformed

    private void cmdNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNextActionPerformed
        // TODO add your handling code here:
        MoveNext();
    }//GEN-LAST:event_cmdNextActionPerformed

    private void cmdBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdBackActionPerformed
        // TODO add your handling code here:
        MovePrevious();
    }//GEN-LAST:event_cmdBackActionPerformed

    private void cmdTopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdTopActionPerformed
        // TODO add your handling code here:
        MoveFirst();
    }//GEN-LAST:event_cmdTopActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList ListLocation;
    private javax.swing.JList ListReason;
    private javax.swing.JToolBar ToolBar;
    private javax.swing.JButton cmdBack;
    private javax.swing.JButton cmdCancel;
    private javax.swing.JButton cmdDelete;
    private javax.swing.JButton cmdEdit;
    private javax.swing.JButton cmdExit;
    private javax.swing.JButton cmdFilter;
    private javax.swing.JButton cmdLast;
    private javax.swing.JButton cmdNew;
    private javax.swing.JButton cmdNext;
    private javax.swing.JButton cmdPreview;
    private javax.swing.JButton cmdPrint;
    private javax.swing.JButton cmdSave;
    private javax.swing.JButton cmdTop;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JTextField txtReasonDesc;
    private javax.swing.JTextField txtReasonID;
    // End of variables declaration//GEN-END:variables
    
    private void EnableToolbar()
 {
     //Puts toolbar in enable mode
     cmdTop.setEnabled(true);
     cmdBack.setEnabled(true);
     cmdNext.setEnabled(true);
     cmdLast.setEnabled(true);
     cmdNew.setEnabled(true);
     cmdEdit.setEnabled(true);
     cmdDelete.setEnabled(true);
     cmdSave.setEnabled(false);
     cmdCancel.setEnabled(false);
     cmdFilter.setEnabled(true);
     cmdPreview.setEnabled(true);
     cmdPrint.setEnabled(true);
     cmdExit.setEnabled(true);
    }
    
    private void DisableToolbar()
 {
     //Puts toolbar in enable mode
     cmdTop.setEnabled(false);
     cmdBack.setEnabled(false);
     cmdNext.setEnabled(false);
     cmdLast.setEnabled(false);
     cmdNew.setEnabled(false);
     cmdEdit.setEnabled(false);
     cmdDelete.setEnabled(false);
     cmdSave.setEnabled(true);
     cmdCancel.setEnabled(true);
     cmdFilter.setEnabled(false);
     cmdPreview.setEnabled(false);
     cmdPrint.setEnabled(false);
     cmdExit.setEnabled(false);
    }
    
    //Didplay data on the Screen
    private void DisplayData()
 {
     txtReasonID.setText(Double.toString(ObjReason.getAttribute("REASON_ID").getVal()));
     txtReasonDesc.setText((String)ObjReason.getAttribute("REASON_DESC").getObj());
    }
    
    //Sets data to the Class Object
    private void SetData()
 {
      ObjReason.setAttribute("REASON_ID",txtReasonID.getText());     
      ObjReason.setAttribute("REASON_DESC",txtReasonDesc.getText());
      if(EditMode==EITLERPGLOBAL.ADD)
      {
          ObjReason.setAttribute("CREATED_BY",EITLERPGLOBAL.gUserID);
          ObjReason.setAttribute("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
      }
      else
      {
          ObjReason.setAttribute("MODIFIED_BY",EITLERPGLOBAL.gUserID);
          ObjReason.setAttribute("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
      }
    }

private void ClearFields()
{
    txtReasonID.setText("");
    txtReasonDesc.setText("");
}

  private void SetFields(boolean pStat)
  {
      txtReasonID.setEnabled(pStat); 
      txtReasonDesc.setEnabled(pStat);
  }    

  private void Cancel()
  {
      DisplayData();
      EditMode=0;
      SetFields(false);
      EnableToolbar();
      ShowMessage("Ready ..........");      
  }
  
  private void MoveFirst()
  {
    ObjReason.MoveFirst();
    DisplayData();
  }
  
  private void MovePrevious()
  {
    ObjReason.MovePrevious();
    DisplayData();
  }
  
  private void MoveNext()
  {
    ObjReason.MoveNext();
    DisplayData();
  }
  
  private void MoveLast()
  {
    ObjReason.MoveLast();
    DisplayData();
  }
    
  private void Add()
  {
     EditMode=EITLERPGLOBAL.ADD;
     DisableToolbar();
     SetFields(true);
     txtReasonID.setEnabled(false);
     ClearFields();
     txtReasonDesc.requestFocus();
  }
  
  private void Edit()
  {
     EditMode=EITLERPGLOBAL.EDIT;
     DisableToolbar();
     SetFields(true);
     txtReasonID.setEnabled(false);    
     txtReasonDesc.requestFocus();
  }
  
  
  private void Delete()
  {
       if(ObjReason.Delete())
       {
        MoveLast();
        DisplayData();
        Generate_List();
       }
       else
       {
          JOptionPane.showMessageDialog(null,"Error occured while deleting. Error is"+ObjReason.LastError);
       }
  }

 private void Save()
  {
      // --------- Form Level Validations ------------ //
      
      if(txtReasonDesc.getText().equals(""))
      {
          JOptionPane.showMessageDialog(null,"Please enter Reasom Description or Reason Name");
          return;
      }
      
      SetData();
      
      if(EditMode==EITLERPGLOBAL.ADD)
      {
        if(ObjReason.Insert())
        {
            MoveLast();
            DisplayData();
        }
        else
        {
          JOptionPane.showMessageDialog(null,"Error occured while saving item. Error is"+ObjReason.LastError);
          return;
        }
      }      

      if(EditMode==EITLERPGLOBAL.EDIT)
      {
       if(ObjReason.Update())  
       {
        //Nothing to do
       }
       else
       {
        JOptionPane.showMessageDialog(null,"Error occured while saving item. Error is"+ObjReason.LastError);
        return;
       }
      }
      
      EditMode=0;
      SetFields(false);
      EnableToolbar();
      Generate_List();
      ShowMessage("Ready ..........");      
  }    

 private void Generate_List()
 {
   HashMap List= new HashMap();
   String strCondition=" WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID;

   EITLListModel aModel=new EITLListModel();
   ListReason.removeAll();
   ListReason.setModel(aModel);
   EITLTreeModel aData;
    
   List = clsReason.getList(strCondition);
   for(int i=1;i<=List.size();i++)
   {
       clsReason ObjReason=(clsReason) List.get(Integer.toString(i));
       int j = (int) ObjReason.getAttribute("REASON_ID").getVal(); 
       aData=new EITLTreeModel(Integer.toString(j),(String) ObjReason.getAttribute("REASON_DESC").getObj());
       aModel.addElement(aData);
    }
  }

 private void SetMenuForRights()
  {
   // --- Add Rights --
   if(clsUser.IsFunctionGranted((int) EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID, 0,191))
   {
      cmdNew.setEnabled(true);
   }
   else
   {
       cmdNew.setEnabled(false);
   }
      
   // --- Edit Rights --
   if(clsUser.IsFunctionGranted((int) EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID, 0,192))
   {
      cmdEdit.setEnabled(true);
   }
   else
   {
       cmdEdit.setEnabled(false);
   }
   
   // --- Delete Rights --
   if(clsUser.IsFunctionGranted((int) EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID, 0,193))
   {
      cmdDelete.setEnabled(true);
   }
   else
   {
      cmdDelete.setEnabled(false);
   }
   
   // --- Print Rights --
   if(clsUser.IsFunctionGranted((int) EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID, 0,194))
   {
      cmdPreview.setEnabled(true);
      cmdPrint.setEnabled(true);
   }
   else
   {
      cmdPreview.setEnabled(false);
      cmdPrint.setEnabled(false);
   }
   }

 private void ShowMessage(String pMessage)
 {
     lblStatus.setText(pMessage);
 }
 
}