/*
 * frmItemCategory.java
 *
 * Created on May 29, 2004, 10:48 AM
 */

/*<APPLET CODE=frmItemCategory.class HEIGHT=350 WIDTH=600></APPLET>*/

package EITLERP;

/** 
 *
 * @author  jadave
 */
import javax.swing.*;
import java.awt.*;
import java.net.*;
import java.sql.*;
import java.text.*;
import java.util.*;
  

public class frmItemCategory extends javax.swing.JApplet {
    
    private int EditMode=0;
    private clsItemCategory ObjItemCategory;
   
    /** Creates new form frmItemCategory */
    public frmItemCategory() {
        System.gc();
        setSize(465,355);
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
       // cmdFilter.setIcon(EITLERPGLOBAL.getImage("FIND"));
        cmdPreview.setIcon(EITLERPGLOBAL.getImage("PREVIEW"));
        cmdPrint.setIcon(EITLERPGLOBAL.getImage("PRINT"));
        cmdExit.setIcon(EITLERPGLOBAL.getImage("EXIT"));
        
        //--- Create instance of Item Object --
        ObjItemCategory=new clsItemCategory();
        
        if(ObjItemCategory.LoadData((int)EITLERPGLOBAL.gCompanyID))
        {
            SetFields(false); 
            //ObjItemCategory.MoveFirst();
            DisplayData();
            Generate_List();
            //------- Set the menu for User rights ----- //
            SetMenuForRights();
            ShowMessage("Ready ............");
        }
        else
        {
            JOptionPane.showMessageDialog(null,"Error loading warehouse data. Error is "+ObjItemCategory.LastError);
        }
        
    }
    
    private void SetFields(boolean pStat)
  {
      txtCID.setEnabled(pStat); 
      txtDesc.setEnabled(pStat);      
  }    
   
 private void SetMenuForRights()
  {
   // --- Add Rights --
   if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID, 0,141))   {
      cmdNew.setEnabled(true);
   }
   else
   {
       cmdNew.setEnabled(false);
   }
      
   // --- Edit Rights --
   if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID, 0,142))
   {
      cmdEdit.setEnabled(true);
   }
   else
   {
       cmdEdit.setEnabled(false);
   }
   
   // --- Delete Rights --
   if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID, 0,143))
   {
      cmdDelete.setEnabled(true);
   }
   else
   {
      cmdDelete.setEnabled(false);
   }
   
   // --- Print Rights --
   if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID, 0,144))
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

  private void Cancel()
  {
      DisplayData();
      EditMode=0;
      SetFields(false);
      EnableToolbar();
      ShowMessage("Ready ............");
  }
  
  private void MoveFirst()
  {
    ObjItemCategory.MoveFirst();
    DisplayData();
  }
  
  private void MovePrevious()
  {
    ObjItemCategory.MovePrevious();
    DisplayData();
  }
  
  private void MoveNext()
  {
    ObjItemCategory.MoveNext();
    DisplayData();
  }
  
  private void MoveLast()
  {
    ObjItemCategory.MoveLast();
    DisplayData();
  }
  
  private void Add()
  {
     EditMode=EITLERPGLOBAL.ADD;
     DisableToolbar();
     SetFields(true);
     ClearFields();
     txtDesc.requestFocus();
  }
  
  private void ClearFields()
{    
    txtCID.setText("");
    txtDesc.setText("");    
}

  private void Edit()
  {
     EditMode=EITLERPGLOBAL.EDIT;
     DisableToolbar();
     SetFields(true);
     txtDesc.requestFocus();
  }
  
  
  private void Delete()
  {
       if(ObjItemCategory.Delete())
       {
        MoveLast();        
        DisplayData();
        Generate_List();
       }
       else
       {
          JOptionPane.showMessageDialog(null,"Error occured while deleting. Error is"+ObjItemCategory.LastError);
       }
  }

  private void Save()
  {
      // --------- Form Level Validations ------------ //
      
      
      if(txtDesc.getText().equals(""))
      {
          JOptionPane.showMessageDialog(null,"Please Enter Item Description");
          return;
      }

      SetData();
      if(EditMode==EITLERPGLOBAL.ADD)
      {
        if(ObjItemCategory.Insert())
        {
            MoveLast();
            DisplayData();
        }
        else
        {
          JOptionPane.showMessageDialog(null,"Error occured while saving item. Error is"+ObjItemCategory.LastError);
          return;
        }
      }      

      if(EditMode==EITLERPGLOBAL.EDIT)
      {
       if(ObjItemCategory.Update())   
       {
        //Nothing to do
       }
       else
       {
        JOptionPane.showMessageDialog(null,"Error occured while saving item. Error is"+ObjItemCategory.LastError);
        return;
       }
      }
      
      EditMode=0;
      SetFields(false);
      EnableToolbar();
      SetMenuForRights();
      Generate_List();
      ShowMessage("Ready ............");
  }    

 private void Generate_List()
 {
   HashMap List=new HashMap();
   String strCondition=" WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID;

   EITLListModel aModel=new EITLListModel();
     
   listCategory.removeAll();
   listCategory.setModel(aModel);
   EITLTreeModel aData;
    //ok
   List = clsItemCategory.getList(strCondition);
   for(int i=1;i<=List.size();i++)
   {
       clsItemCategory ObjICategory=(clsItemCategory) List.get(Integer.toString(i));
       aData=new EITLTreeModel(Integer.toString((int)ObjICategory.getAttribute("CATEGORY_ID").getVal()),(String)ObjICategory.getAttribute("CATEGORY_DESC").getObj());
       aModel.addElement(aData);
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
        cmdPreview = new javax.swing.JButton();
        cmdPrint = new javax.swing.JButton();
        cmdExit = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtCID = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        listCategory = new javax.swing.JList();
        txtDesc = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
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

        cmdNew.setToolTipText("Add Record");
        cmdNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNewActionPerformed(evt);
            }
        });

        ToolBar.add(cmdNew);

        cmdEdit.setToolTipText("Edit Record");
        cmdEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdEditActionPerformed(evt);
            }
        });

        ToolBar.add(cmdEdit);

        cmdDelete.setToolTipText("Delete Record");
        cmdDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdDeleteActionPerformed(evt);
            }
        });

        ToolBar.add(cmdDelete);

        cmdSave.setToolTipText("Save Record");
        cmdSave.setEnabled(false);
        cmdSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdSaveActionPerformed(evt);
            }
        });

        ToolBar.add(cmdSave);

        cmdCancel.setToolTipText("Cancel Record");
        cmdCancel.setEnabled(false);
        cmdCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCancelActionPerformed(evt);
            }
        });

        ToolBar.add(cmdCancel);

        ToolBar.add(cmdPreview);

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

        jPanel1.setLayout(null);

        jPanel1.setBorder(new javax.swing.border.EtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel2.setText("Category ID");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(170, 60, 70, 15);

        txtCID.setEditable(false);
        txtCID.setBorder(new javax.swing.border.EtchedBorder());
        jPanel1.add(txtCID);
        txtCID.setBounds(240, 60, 82, 22);

        jLabel3.setText("Description");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(170, 100, 70, 15);

        jScrollPane1.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(1, 1, 1, 1)));
        listCategory.setBorder(new javax.swing.border.EtchedBorder());
        listCategory.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        listCategory.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                listCategoryMouseClicked(evt);
            }
        });

        jScrollPane1.setViewportView(listCategory);

        jPanel1.add(jScrollPane1);
        jScrollPane1.setBounds(10, 10, 150, 220);

        txtDesc.setBorder(new javax.swing.border.EtchedBorder());
        txtDesc.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtDescFocusGained(evt);
            }
        });

        jPanel1.add(txtDesc);
        txtDesc.setBounds(240, 100, 190, 50);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(0, 66, 450, 240);

        jLabel1.setBackground(new java.awt.Color(0, 102, 153));
        jLabel1.setForeground(java.awt.Color.white);
        jLabel1.setText("ITEM CATEGORY");
        jLabel1.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel1.setOpaque(true);
        getContentPane().add(jLabel1);
        jLabel1.setBounds(0, 40, 650, 25);

        lblStatus.setFont(new java.awt.Font("Tahoma", 1, 12));
        lblStatus.setForeground(new java.awt.Color(51, 51, 255));
        lblStatus.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(lblStatus);
        lblStatus.setBounds(0, 310, 450, 22);

    }//GEN-END:initComponents

    private void txtDescFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDescFocusGained
        // TODO add your handling code here:
        ShowMessage("Enter Category Description .........");
    }//GEN-LAST:event_txtDescFocusGained

    private void listCategoryMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listCategoryMouseClicked
        // TODO add your handling code here:
        EITLTreeModel aData = new EITLTreeModel("","");
        aData=(EITLTreeModel)listCategory.getModel().getElementAt(listCategory.getSelectedIndex());
        
        
        
        /*ObjWarehouse.Filter(" WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND TRIM(WAREHOUSE_ID)='"+aData.Key+"'");
        DisplayData();
        ObjWarehouse.Filter(" WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+"");*/
        ObjItemCategory.Find(aData.Key);
        DisplayData();
    }//GEN-LAST:event_listCategoryMouseClicked

    private void cmdExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdExitActionPerformed
        // TODO add your handling code here:
        ObjItemCategory.Close();
        ((JFrame)getParent().getParent().getParent().getParent()).dispose();
    }//GEN-LAST:event_cmdExitActionPerformed

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

    private void cmdEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdEditActionPerformed
        // TODO add your handling code here:
        Edit();
    }//GEN-LAST:event_cmdEditActionPerformed

    private void cmdNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNewActionPerformed
        // TODO add your handling code here:
        Add();
    }//GEN-LAST:event_cmdNewActionPerformed

    private void cmdLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdLastActionPerformed
        // TODO add your handling code here:
        MoveLast();
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
    private javax.swing.JToolBar ToolBar;
    private javax.swing.JButton cmdBack;
    private javax.swing.JButton cmdCancel;
    private javax.swing.JButton cmdDelete;
    private javax.swing.JButton cmdEdit;
    private javax.swing.JButton cmdExit;
    private javax.swing.JButton cmdLast;
    private javax.swing.JButton cmdNew;
    private javax.swing.JButton cmdNext;
    private javax.swing.JButton cmdPreview;
    private javax.swing.JButton cmdPrint;
    private javax.swing.JButton cmdSave;
    private javax.swing.JButton cmdTop;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JList listCategory;
    private javax.swing.JTextField txtCID;
    private javax.swing.JTextArea txtDesc;
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
     //cmdFilter.setEnabled(true);
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
     //cmdFilter.setEnabled(false);
     cmdPreview.setEnabled(false);
     cmdPrint.setEnabled(false);
     cmdExit.setEnabled(false);
    }
    
    //Didplay data on the Screen
  private void DisplayData()
 {     
     txtCID.setText(Integer.toString((int)ObjItemCategory.getAttribute("CATEGORY_ID").getVal())) ;
     txtDesc.setText((String)ObjItemCategory.getAttribute("CATEGORY_DESC").getObj());     
     
     
  }
    
    //Sets data to the Class Object
 private void SetData()
 {
      ObjItemCategory.setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
      ObjItemCategory.setAttribute("CATEGORY_ID",txtCID.getText() );
      ObjItemCategory.setAttribute("CATEGORY_DESC",txtDesc.getText());     
      
      if(EditMode==EITLERPGLOBAL.ADD)
      {
          ObjItemCategory.setAttribute("CREATED_BY",EITLERPGLOBAL.gUserID);
          ObjItemCategory.setAttribute("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
      }
      else
      {
          ObjItemCategory.setAttribute("MODIFIED_BY",EITLERPGLOBAL.gUserID);
          ObjItemCategory.setAttribute("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
      }
 }

 private void ShowMessage(String pMessage)
 {
     lblStatus.setText(pMessage);
 }
 
}
