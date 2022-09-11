/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales.common;

import EITLERP.EITLERPGLOBAL;
import EITLERP.data;
import java.sql.ResultSet;

/**
 *
 * @author root
 */
public class WIPAmendImport extends javax.swing.JApplet {

    /**
     * Initializes the applet WIPAmendImport
     */
    @Override
    public void init() {
        
        initComponents();
        
        runProcess();
                
        
    }
    private void runProcess()
    {
       try{
            ResultSet result = data.getResult("SELECT * FROM PRODUCTION.FELT_SALES_WIP_PIECE_TEMP");
            result.first();
        
            int i=1;
            while(!result.isAfterLast())
            {
                if(result.getString("ACTION_PERFORMED").equals(""))
                {
                String DOC_NO = result.getString("DOC_NO");
                String DOC_DATE = result.getString("DOC_DATE");
                String PARTY_CODE = result.getString("PARTY_CODE");
                
                String CHANGE_POSIBILITY = result.getString("CHANGE_POSIBILITY");
                String DELINK = result.getString("DELINK");
                String ACTUAL_CHANGE = result.getString("ACTUAL_CHANGE");
                String CANCELLED = result.getString("CANCELLED");
                String DUPLICATE = result.getString("DUPLICATE");
                
                String PIECE_NO = result.getString("PIECE_NO");
                String PIECE_STAGE = result.getString("PIECE_STAGE");

                String LENGTH = result.getString("LENGTH");
                String LENGTH_UPDATED = result.getString("LENGTH_UPDATED");
                String WIDTH = result.getString("WIDTH");
                String WIDTH_UPDATED = result.getString("WIDTH_UPDATED");
                String GSM = result.getString("GSM");
                String GSM_UPDATED = result.getString("GSM_UPDATED");
                String STYLE = result.getString("STYLE");
                String STYLE_UPDATED = result.getString("STYLE_UPDATED");
                String WEIGHT = result.getString("WEIGHT");
                String WEIGHT_UPDATED = result.getString("WEIGHT_UPDATED");
                String SQMTR = result.getString("SQMTR");
                String SQMTR_UPDATED = result.getString("SQMTR_UPDATED");
                
                try{
                        data.Execute("UPDATE PRODUCTION.FELT_PROD_DOC_DATA  SET STATUS='A' WHERE MODULE_ID=750 AND DOC_NO='"+DOC_NO+"'");
                        int max = data.getIntValueFromDB("SELECT MAX(SR_NO) FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE DOC_NO='"+DOC_NO+"' AND MODULE_ID=750");
                        data.Execute("UPDATE PRODUCTION.FELT_PROD_DOC_DATA  SET STATUS='F' WHERE MODULE_ID=750 AND DOC_NO='"+DOC_NO+"' AND SR_NO=("+max+")");
                        data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_AMEND_APPROVAL_HEADER SET APPROVED=1,APPROVED_BY=0,APPROVED_DATE='"+EITLERPGLOBAL.getCurrentDateDB()+"' WHERE  PIECE_AMEND_NO='"+DOC_NO+"' AND MM_PARTY_CODE='"+PARTY_CODE+"'");
                
                        if(CHANGE_POSIBILITY.equals("YES") && ACTUAL_CHANGE.equals("YES"))
                        {
                            try{
                                data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET "
                                      + "PR_LENGTH='"+LENGTH_UPDATED+"',"
                                      + "PR_WIDTH='"+WIDTH_UPDATED+"',"
                                      + "PR_GSM='"+GSM_UPDATED+"',"
                                      + "PR_THORITICAL_WEIGHT='"+WEIGHT_UPDATED+"',"
                                      + "PR_SQMTR='"+SQMTR_UPDATED+"',"
                                      + "PR_STYLE='"+STYLE_UPDATED+"' "
                                      + "where PR_PIECE_NO='"+PIECE_NO+"'");
                                data.Execute("UPDATE PRODUCTION.FELT_SALES_WIP_PIECE_TEMP SET ACTION_PERFORMED='DATA CHANGED',STATUS='SUCCRESS' WHERE DOC_NO='"+DOC_NO+"' AND PARTY_CODE='"+PARTY_CODE+"' AND PIECE_NO='"+PIECE_NO+"'");

                            }catch(Exception e)
                            {
                                data.Execute("UPDATE PRODUCTION.FELT_SALES_WIP_PIECE_TEMP SET ACTION_PERFORMED='ERROR ON UPDATE QUERY',STATUS='NOT UPDATED' WHERE DOC_NO='"+DOC_NO+"' AND PARTY_CODE='"+PARTY_CODE+"' AND PIECE_NO='"+PIECE_NO+"'");
                                e.printStackTrace();
                            }
                        }
                        else if(DELINK.equals("YES"))
                        {
                            try{
                                data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET "
                                      + "PR_DELINK='DELINK' "
                                      + "where PR_PIECE_NO='"+PIECE_NO+"'");
                                
                                data.Execute("UPDATE PRODUCTION.FELT_SALES_WIP_PIECE_TEMP SET ACTION_PERFORMED='DELINKED',STATUS='SUCCRESS' WHERE DOC_NO='"+DOC_NO+"' AND PARTY_CODE='"+PARTY_CODE+"' AND PIECE_NO='"+PIECE_NO+"'");

                            }catch(Exception e)
                            {
                                data.Execute("UPDATE PRODUCTION.FELT_SALES_WIP_PIECE_TEMP SET ACTION_PERFORMED='ERROR ON DELINK',STATUS='NOT DELINKED' WHERE DOC_NO='"+DOC_NO+"' AND PARTY_CODE='"+PARTY_CODE+"' AND PIECE_NO='"+PIECE_NO+"'");
                                e.printStackTrace();
                            }
                        }
                        else if(CANCELLED.equals("YES"))
                        {
                            data.Execute("UPDATE PRODUCTION.FELT_SALES_WIP_PIECE_TEMP SET ACTION_PERFORMED='NO ACTION',STATUS='NO ACTION' WHERE DOC_NO='"+DOC_NO+"' AND PARTY_CODE='"+PARTY_CODE+"' AND PIECE_NO='"+PIECE_NO+"'");
                        }
                        else if(DUPLICATE.equals("YES"))
                        {
                            data.Execute("UPDATE PRODUCTION.FELT_SALES_WIP_PIECE_TEMP SET ACTION_PERFORMED='NO ACTION',STATUS='DUPLICATE' WHERE DOC_NO='"+DOC_NO+"' AND PARTY_CODE='"+PARTY_CODE+"' AND PIECE_NO='"+PIECE_NO+"'");

                        }
                        else
                        {
                            data.Execute("UPDATE PRODUCTION.FELT_SALES_WIP_PIECE_TEMP SET ACTION_PERFORMED='NO ACTION',STATUS='NO' WHERE DOC_NO='"+DOC_NO+"' AND PARTY_CODE='"+PARTY_CODE+"' AND PIECE_NO='"+PIECE_NO+"'");
                        }
                
                        System.out.println("DOCUMENT = "+DOC_NO+" , PARTY CODE : "+PARTY_CODE+" , STATUS : OK - "+i);
                }catch(Exception e)
                {
                    System.out.println("DOCUMENT = "+DOC_NO+" , PARTY CODE : "+PARTY_CODE+" , STATUS : NOT OK - "+i);
                    e.printStackTrace();
                }
                }
                
                i++;
                result.next();
            }
        
       }catch(Exception e)
       {
           e.printStackTrace();
       }
        
        
    }
    /**
     * This method is called from within the init() method to initialize the
     * form. WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();

        getContentPane().setLayout(null);

        jButton1.setText("Process");
        getContentPane().add(jButton1);
        jButton1.setBounds(210, 30, 62, 29);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    // End of variables declaration//GEN-END:variables
}
