/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales.PieceAmendmentApproval;

import EITLERP.EITLERPGLOBAL;
import EITLERP.FeltSales.Order.clsFeltOrder;
import EITLERP.Variant;
import EITLERP.data;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import javax.swing.JOptionPane;

/**
 *
 * @author DAXESH PRAJAPATI
 */
public class clsPieceMaster {

    private HashMap props;
    public boolean Ready = false;

    public HashMap hmPieceDetail;

    private ResultSet resultSet;
    private static Connection connection;
    private Statement statement;
    public String LastError = "";

    public Variant getAttribute(String PropName) {
        return (Variant) props.get(PropName);
    }

    public void setAttribute(String PropName, Object Value) {
        props.put(PropName, new Variant(Value));
    }

    public void setAttribute(String PropName, int Value) {

        props.put(PropName, new Variant(Value));
    }

    public void setAttribute(String PropName, long Value) {
        props.put(PropName, new Variant(Value));
    }

    public void setAttribute(String PropName, double Value) {
        props.put(PropName, new Variant(Value));
    }

    public void setAttribute(String PropName, float Value) {
        props.put(PropName, new Variant(Value));
    }

    public void setAttribute(String PropName, boolean Value) {
        props.put(PropName, new Variant(Value));
    }

       public clsPieceMaster() {
        props = new HashMap();

        props.put("PR_PIECE_NO", new Variant(""));
        props.put("PR_DATE", new Variant(""));
        props.put("PR_ORDER_DATE", new Variant(""));
        props.put("PR_DOC_NO", new Variant(""));
        props.put("PR_MACHINE_NO", new Variant(""));
        props.put("PR_POSITION_NO", new Variant(""));
        props.put("PR_PARTY_CODE", new Variant(""));
        props.put("PR_PRODUCT_CODE", new Variant(""));
        props.put("PR_GROUP", new Variant(""));
        props.put("PR_STYLE", new Variant(""));
        props.put("PR_LENGTH", new Variant(""));
        props.put("PR_WIDTH", new Variant(""));
        props.put("PR_GSM", new Variant(""));
        props.put("PR_THORITICAL_WEIGHT", new Variant(""));
        props.put("PR_SQMTR", new Variant(""));
        props.put("PR_SYN_PER", new Variant(""));
        props.put("PR_REQUESTED_MONTH", new Variant(""));
        props.put("PR_REGION", new Variant(""));
        props.put("PR_INCHARGE", new Variant(""));
        props.put("PR_REFERENCE", new Variant(""));
        props.put("PR_REFERENCE_DATE", new Variant(""));
        props.put("PR_PO_NO", new Variant(""));
        props.put("PR_PO_DATE", new Variant(""));
        props.put("PR_ORDER_REMARK", new Variant(""));
        props.put("PR_PIECE_REMARK", new Variant(""));
        props.put("PR_PIECE_STAGE", new Variant(""));
        props.put("PR_WARP_DATE", new Variant(""));
        props.put("PR_WVG_DATE", new Variant(""));
        props.put("PR_MND_DATE", new Variant(""));
        props.put("PR_NDL_DATE", new Variant(""));
        props.put("PR_FNSG_DATE", new Variant(""));
        props.put("PR_RCV_DATE", new Variant(""));
        props.put("PR_ACTUAL_WEIGHT", new Variant(""));
        props.put("PR_ACTUAL_LENGTH", new Variant(""));
        props.put("PR_ACTUAL_WIDTH", new Variant(""));
        props.put("PR_BALE_NO", new Variant(""));
        props.put("PR_PACKED_DATE", new Variant(""));
        props.put("PR_REJECTED_FLAG", new Variant(""));
        props.put("PR_REJECTED_REMARK", new Variant(""));
        props.put("PR_DIVERSION_FLAG", new Variant(""));
        props.put("PR_DIVERSION_REASON", new Variant(""));
        props.put("PR_EXP_DISPATCH_DATE", new Variant(""));
        props.put("PR_PRIORITY_HOLD_CAN_FLAG", new Variant(""));
        props.put("PR_INVOICE_NO", new Variant(""));
        props.put("PR_INVOICE_DATE", new Variant(""));
        props.put("PR_LR_NO", new Variant(""));
        props.put("PR_LR_DATE", new Variant(""));
        props.put("PR_INVOICE_PARTY", new Variant(""));
        props.put("PR_PARTY_CODE_ORIGINAL", new Variant(""));
        props.put("PR_PIECE_NO_ORIGINAL", new Variant(""));
        props.put("PR_WH_CODE", new Variant(""));
        props.put("PR_INWARD_NO", new Variant(""));
        props.put("PR_RACK_NO", new Variant(""));
        props.put("PR_PIECE_ID", new Variant(""));
        props.put("PR_LOCATION", new Variant(""));
        props.put("PR_HOLD_DATE", new Variant(""));
        props.put("PR_HOLD_REASON", new Variant(""));
        props.put("PR_RELEASE_DATE", new Variant(""));
        props.put("CREATED_DATE", new Variant(""));
        props.put("CREATED_BY", new Variant(""));
        props.put("MODIFIED_DATE", new Variant(""));
        props.put("MODIFIED_BY", new Variant(""));
        props.put("HIERARCHY_ID", new Variant(""));
        props.put("APPROVER_BY", new Variant(""));
        props.put("APPROVER_DATE", new Variant(""));
        props.put("APPROVER_REMARK", new Variant(""));

        hmPieceDetail = new HashMap();
    }

    public HashMap getPieceList(String Party_Code, String MachineNo, String Position) {
        Ready = false;

        String SQL = "SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PARTY_CODE = '"+Party_Code+"' AND PR_MACHINE_NO='"+MachineNo+"' AND PR_POSITION_NO='"+Position+"' AND PR_PIECE_STAGE NOT IN ('STOCK',' IN STOCK',' DESPATCHED')";
        
        System.out.println("SQL = " + SQL);

        HashMap hmPieceList = new HashMap();

        try {
            connection = data.getConn();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            resultSet = statement.executeQuery(SQL);
            Ready = true;

            while (resultSet.next()) {
                clsPieceMaster piece = new clsPieceMaster();

                String Length="",Width="",Gsm="",Style="",Pr_Group="";
               try{ 
                        String strSQL="";
                        ResultSet rsTmp;
                        strSQL= "";
                        
                        strSQL="SELECT MM_MACHINE_NO,MM_MACHINE_POSITION,MM_MACHINE_POSITION_DESC,MM_ITEM_CODE,MM_GRUP,MM_FELT_LENGTH,MM_FELT_WIDTH,MM_FELT_GSM,MM_FELT_STYLE FROM PRODUCTION.FELT_MACHINE_MASTER_DETAIL where MM_PARTY_CODE='"+Party_Code+"' AND MM_MACHINE_NO = '"+MachineNo+"' AND MM_MACHINE_POSITION = '"+Position+"'";
                        System.out.println(" Check to Machine No Query : "+strSQL);  
                        
                        rsTmp=data.getResult(strSQL);
                        rsTmp.first();

                        Length = rsTmp.getString("MM_FELT_LENGTH");
                        Width = rsTmp.getString("MM_FELT_WIDTH");
                        Gsm = rsTmp.getString("MM_FELT_GSM");
                        Style = rsTmp.getString("MM_FELT_STYLE");
                        Pr_Group = rsTmp.getString("MM_GRUP");
                
                  }catch(Exception ew)
                  {
                      System.out.println("Error on getting data");
                  }
               
                boolean flag= true;
                
                
//                JOptionPane.showMessageDialog(null, "Length : "+resultSet.getString("PR_LENGTH")+" , "+Length);
//                JOptionPane.showMessageDialog(null, "Width : "+resultSet.getString("PR_WIDTH")+" , "+Width);
//                JOptionPane.showMessageDialog(null, "Gsm : "+resultSet.getString("PR_GSM")+" , "+Gsm);
//                JOptionPane.showMessageDialog(null, "Style : "+resultSet.getString("PR_STYLE")+" , "+Style);
//                JOptionPane.showMessageDialog(null, "GRUP : "+resultSet.getString("PR_GROUP")+" , "+Pr_Group);
                
                float l1 = Float.parseFloat(resultSet.getString("PR_LENGTH"));
                float l2 = Float.parseFloat(Length);
                
                float w1 = Float.parseFloat(resultSet.getString("PR_WIDTH"));
                float w2 = Float.parseFloat(Width);
                
                float g1 = Float.parseFloat(resultSet.getString("PR_GSM"));
                float g2 = Float.parseFloat(Gsm);
                
                
                if(l1 == l2 && w1 == w2 && g1 == g2 && resultSet.getString("PR_STYLE").equals(Style) && resultSet.getString("PR_GROUP").equals(Pr_Group))
                {
                    flag=false;
                }
                if(flag)
                {
                    piece.setAttribute("PR_PIECE_NO", resultSet.getString("PR_PIECE_NO"));
                    piece.setAttribute("PR_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_DATE")));
                    piece.setAttribute("PR_ORDER_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_ORDER_DATE")));
                    piece.setAttribute("PR_DOC_NO", resultSet.getString("PR_DOC_NO"));
                    piece.setAttribute("PR_MACHINE_NO", resultSet.getString("PR_MACHINE_NO"));
                    piece.setAttribute("PR_POSITION_NO", resultSet.getString("PR_POSITION_NO"));
                    piece.setAttribute("PR_PARTY_CODE", resultSet.getString("PR_PARTY_CODE"));
                    piece.setAttribute("PR_PRODUCT_CODE", resultSet.getString("PR_PRODUCT_CODE"));
                    piece.setAttribute("PR_GROUP", resultSet.getString("PR_GROUP"));
                    piece.setAttribute("PR_STYLE", resultSet.getString("PR_STYLE"));
                    piece.setAttribute("PR_LENGTH", resultSet.getString("PR_LENGTH"));
                    piece.setAttribute("PR_WIDTH", resultSet.getString("PR_WIDTH"));
                    piece.setAttribute("PR_GSM", resultSet.getString("PR_GSM"));
                    piece.setAttribute("PR_THORITICAL_WEIGHT", resultSet.getString("PR_THORITICAL_WEIGHT"));
                    piece.setAttribute("PR_SQMTR", resultSet.getString("PR_SQMTR"));
                    piece.setAttribute("PR_SYN_PER", resultSet.getString("PR_SYN_PER"));
                    piece.setAttribute("PR_REQUESTED_MONTH", resultSet.getString("PR_REQUESTED_MONTH"));
                    piece.setAttribute("PR_REGION", resultSet.getString("PR_REGION"));
                    piece.setAttribute("PR_INCHARGE", resultSet.getString("PR_INCHARGE"));
                    piece.setAttribute("PR_REFERENCE", resultSet.getString("PR_REFERENCE"));
                    piece.setAttribute("PR_REFERENCE_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_REFERENCE_DATE")));
                    piece.setAttribute("PR_PO_NO", resultSet.getString("PR_PO_NO"));
                    piece.setAttribute("PR_PO_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_PO_DATE")));
                    piece.setAttribute("PR_ORDER_REMARK", resultSet.getString("PR_ORDER_REMARK"));
                    piece.setAttribute("PR_PIECE_REMARK", resultSet.getString("PR_PIECE_REMARK"));
                    piece.setAttribute("PR_PIECE_STAGE", resultSet.getString("PR_PIECE_STAGE"));
                    piece.setAttribute("PR_WARP_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_WARP_DATE")));
                    piece.setAttribute("PR_WVG_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_WVG_DATE")));
                    piece.setAttribute("PR_MND_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_MND_DATE")));
                    piece.setAttribute("PR_NDL_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_NDL_DATE")));
                    piece.setAttribute("PR_FNSG_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_FNSG_DATE")));
                    piece.setAttribute("PR_RCV_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_RCV_DATE")));
                    piece.setAttribute("PR_ACTUAL_WEIGHT", resultSet.getString("PR_ACTUAL_WEIGHT"));
                    piece.setAttribute("PR_ACTUAL_LENGTH", resultSet.getString("PR_ACTUAL_LENGTH"));
                    piece.setAttribute("PR_ACTUAL_WIDTH", resultSet.getString("PR_ACTUAL_WIDTH"));
                    piece.setAttribute("PR_BALE_NO", resultSet.getString("PR_BALE_NO"));
                    piece.setAttribute("PR_PACKED_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_PACKED_DATE")));
                    piece.setAttribute("PR_REJECTED_FLAG", resultSet.getString("PR_REJECTED_FLAG"));
                    piece.setAttribute("PR_REJECTED_REMARK", resultSet.getString("PR_REJECTED_REMARK"));
                    piece.setAttribute("PR_DIVERSION_FLAG", resultSet.getString("PR_DIVERSION_FLAG"));
                    piece.setAttribute("PR_DIVERSION_REASON", resultSet.getString("PR_DIVERSION_REASON"));
                    piece.setAttribute("PR_EXP_DISPATCH_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_EXP_DISPATCH_DATE")));
                    piece.setAttribute("PR_PRIORITY_HOLD_CAN_FLAG", resultSet.getString("PR_PRIORITY_HOLD_CAN_FLAG"));
                    piece.setAttribute("PR_INVOICE_NO", resultSet.getString("PR_INVOICE_NO"));
                    piece.setAttribute("PR_INVOICE_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_INVOICE_DATE")));
                    piece.setAttribute("PR_LR_NO", resultSet.getString("PR_LR_NO"));
                    piece.setAttribute("PR_LR_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_LR_DATE")));
                    piece.setAttribute("PR_INVOICE_PARTY", resultSet.getString("PR_INVOICE_PARTY"));
                    piece.setAttribute("PR_PARTY_CODE_ORIGINAL", resultSet.getString("PR_PARTY_CODE_ORIGINAL"));
                    piece.setAttribute("PR_PIECE_NO_ORIGINAL", resultSet.getString("PR_PIECE_NO_ORIGINAL"));
                    piece.setAttribute("PR_WH_CODE", resultSet.getString("PR_WH_CODE"));
                    piece.setAttribute("PR_INWARD_NO", resultSet.getString("PR_INWARD_NO"));
                    piece.setAttribute("PR_RACK_NO", resultSet.getString("PR_RACK_NO"));
                    piece.setAttribute("PR_PIECE_ID", resultSet.getString("PR_PIECE_ID"));
                    piece.setAttribute("PR_LOCATION", resultSet.getString("PR_LOCATION"));
                    piece.setAttribute("PR_HOLD_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_HOLD_DATE")));
                    piece.setAttribute("PR_HOLD_REASON", resultSet.getString("PR_HOLD_REASON"));
                    piece.setAttribute("PR_RELEASE_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_RELEASE_DATE")));
                    piece.setAttribute("PR_PIECE_STAGE", resultSet.getString("PR_PIECE_STAGE"));
                    piece.setAttribute("CREATED_DATE", resultSet.getString("CREATED_DATE"));
                    piece.setAttribute("CREATED_BY", resultSet.getString("CREATED_BY"));
                    piece.setAttribute("MODIFIED_DATE", resultSet.getString("MODIFIED_DATE"));
                    piece.setAttribute("MODIFIED_BY", resultSet.getString("MODIFIED_BY"));
                    piece.setAttribute("HIERARCHY_ID", resultSet.getString("HIERARCHY_ID"));
                    piece.setAttribute("APPROVER_BY", resultSet.getString("APPROVER_BY"));
                    piece.setAttribute("APPROVER_DATE", resultSet.getString("APPROVER_DATE"));
                    piece.setAttribute("APPROVER_REMARK", resultSet.getString("APPROVER_REMARK"));

                    hmPieceList.put(hmPieceList.size() + 1, piece);
                }
                
            }

            return hmPieceList;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Exceoption " + e.getMessage());
            e.printStackTrace();
            LastError = e.getMessage();
            return hmPieceList;
        }
    }

}
