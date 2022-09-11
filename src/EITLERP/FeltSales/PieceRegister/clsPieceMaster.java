/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales.PieceRegister;

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
import EITLERP.*;
import java.sql.SQLException;

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
        props.put("PR_UPN", new Variant(""));

        hmPieceDetail = new HashMap();
    }

    public HashMap ShowAllData() {
        Ready = false;
        HashMap hmPieceList = new HashMap();

        try {
            connection = data.getConn();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            resultSet = statement.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER");
            Ready = true;

            while (resultSet.next()) {
                clsPieceMaster piece = new clsPieceMaster();

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
                piece.setAttribute("PR_REFERENCE_DATE", resultSet.getString("PR_REFERENCE_DATE"));
                piece.setAttribute("PR_PO_NO", resultSet.getString("PR_PO_NO"));
                piece.setAttribute("PR_PO_DATE", resultSet.getString("PR_PO_DATE"));
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
                piece.setAttribute("PR_PACKED_DATE", resultSet.getString("PR_PACKED_DATE"));
                piece.setAttribute("PR_REJECTED_FLAG", resultSet.getString("PR_REJECTED_FLAG"));
                piece.setAttribute("PR_REJECTED_REMARK", resultSet.getString("PR_REJECTED_REMARK"));
                piece.setAttribute("PR_DIVERSION_FLAG", resultSet.getString("PR_DIVERSION_FLAG"));
                piece.setAttribute("PR_DIVERSION_REASON", resultSet.getString("PR_DIVERSION_REASON"));
                piece.setAttribute("PR_EXP_DISPATCH_DATE", resultSet.getString("PR_EXP_DISPATCH_DATE"));
                piece.setAttribute("PR_PRIORITY_HOLD_CAN_FLAG", resultSet.getString("PR_PRIORITY_HOLD_CAN_FLAG"));
                piece.setAttribute("PR_INVOICE_NO", resultSet.getString("PR_INVOICE_NO"));
                piece.setAttribute("PR_INVOICE_DATE", resultSet.getString("PR_INVOICE_DATE"));
                piece.setAttribute("PR_LR_NO", resultSet.getString("PR_LR_NO"));
                piece.setAttribute("PR_LR_DATE", resultSet.getString("PR_LR_DATE"));
                piece.setAttribute("PR_INVOICE_PARTY", resultSet.getString("PR_INVOICE_PARTY"));
                piece.setAttribute("PR_PARTY_CODE_ORIGINAL", resultSet.getString("PR_PARTY_CODE_ORIGINAL"));
                piece.setAttribute("PR_PIECE_NO_ORIGINAL", resultSet.getString("PR_PIECE_NO_ORIGINAL"));
                piece.setAttribute("PR_WH_CODE", resultSet.getString("PR_WH_CODE"));
                piece.setAttribute("PR_INWARD_NO", resultSet.getString("PR_INWARD_NO"));
                piece.setAttribute("PR_RACK_NO", resultSet.getString("PR_RACK_NO"));
                piece.setAttribute("PR_PIECE_ID", resultSet.getString("PR_PIECE_ID"));
                piece.setAttribute("PR_LOCATION", resultSet.getString("PR_LOCATION"));
                piece.setAttribute("PR_HOLD_DATE", resultSet.getString("PR_HOLD_DATE"));
                piece.setAttribute("PR_HOLD_REASON", resultSet.getString("PR_HOLD_REASON"));
                piece.setAttribute("PR_RELEASE_DATE", resultSet.getString("PR_RELEASE_DATE"));
                piece.setAttribute("CREATED_DATE", resultSet.getString("CREATED_DATE"));
                piece.setAttribute("CREATED_BY", resultSet.getString("CREATED_BY"));
                piece.setAttribute("MODIFIED_DATE", resultSet.getString("MODIFIED_DATE"));
                piece.setAttribute("MODIFIED_BY", resultSet.getString("MODIFIED_BY"));
                piece.setAttribute("HIERARCHY_ID", resultSet.getString("HIERARCHY_ID"));
                piece.setAttribute("APPROVER_BY", resultSet.getString("APPROVER_BY"));
                piece.setAttribute("APPROVER_DATE", resultSet.getString("APPROVER_DATE"));
                piece.setAttribute("APPROVER_REMARK", resultSet.getString("APPROVER_REMARK"));
                
                piece.setAttribute("PR_UPN", resultSet.getString("PR_UPN"));

                hmPieceList.put(hmPieceList.size() + 1, piece);
            }

            return hmPieceList;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Exceoption " + e.getMessage());
            e.printStackTrace();
            LastError = e.getMessage();
            return hmPieceList;
        }
    }

    public HashMap getAdvSearch(String Party_Code, String Length_from, String Length_to, String Width_from, String Width_to, String GSM_from, String GSM_to, String Piece_No, String Product_Code, String Zone, String Incharge, String From_date, String To_date, String MachineNo, String Position, String UPN, String PIECE_STAGE,String ORDER_BY) {
        Ready = false;

//        String SQL = "SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER  WHERE PR_PARTY_CODE!='' AND PR_PRIORITY_HOLD_CAN_FLAG IN (0,1,2,3,4,5) ";
        String SQL = "SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER  WHERE PR_PARTY_CODE!='' ";
        boolean flag = false;

        if (!Party_Code.equals("")) {
            //criteria.add(Restrictions.eq("prPartyCode", Party_Code));
            SQL = SQL + " AND PR_PARTY_CODE = '" + Party_Code + "' ";
            flag = true;
        }

        if (!Length_from.equals("") && !Length_to.equals("")) {
               //  criteria.add(Restrictions.between("prLength", Float.parseFloat(Length_from), Float.parseFloat(Length_to)));

            SQL = SQL + " AND (PR_LENGTH BETWEEN  '" + Length_from + "' AND '" + Length_to + "') ";
            flag = true;
        } else if (!Length_from.equals("") && Length_to.equals("")) {
            // criteria.add(Restrictions.gt("prLength", Float.parseFloat(Length_from)));
            SQL = SQL + " AND PR_LENGTH >= '" + Length_from + "' ";
            flag = true;
        } else if (Length_from.equals("") && !Length_to.equals("")) {
            // criteria.add(Restrictions.lt("prLength", Float.parseFloat(Length_to)));
            SQL = SQL + " AND PR_LENGTH <= '" + Length_to + "' ";
            flag = true;
        }

        if (!Width_from.equals("") && !Width_to.equals("")) {
            //  criteria.add(Restrictions.between("prWidth", Float.parseFloat(Width_from), Float.parseFloat(Width_to)));
            SQL = SQL + " AND (PR_WIDTH BETWEEN  '" + Width_from + "' AND '" + Width_to + "') ";
            flag = true;
        } else if (!Width_from.equals("") && Width_to.equals("")) {
            // criteria.add(Restrictions.gt("prWidth", Float.parseFloat(Width_from)));
            SQL = SQL + " AND PR_WIDTH >= '" + Width_from + "' ";
            flag = true;
        } else if (Width_from.equals("") && !Width_to.equals("")) {
            // criteria.add(Restrictions.lt("prWidth", Float.parseFloat(Width_to)));
            SQL = SQL + " AND PR_WIDTH <= '" + Width_to + "' ";
            flag = true;
        }

        if (!GSM_from.equals("") && !GSM_to.equals("")) {
            // criteria.add(Restrictions.between("prGsm", Float.parseFloat(GSM_from), Float.parseFloat(GSM_to)));
            SQL = SQL + " AND (PR_GSM BETWEEN  '" + GSM_from + "' AND '" + GSM_to + "') ";
            flag = true;
        } else if (!GSM_from.equals("") && GSM_to.equals("")) {
            // criteria.add(Restrictions.gt("prGsm", Float.parseFloat(GSM_from)));
            SQL = SQL + " AND PR_GSM >= '" + GSM_from + "' ";
            flag = true;
        } else if (GSM_from.equals("") && !GSM_to.equals("")) {
            // criteria.add(Restrictions.lt("prGsm", Float.parseFloat(GSM_to)));
            SQL = SQL + " AND PR_GSM <= '" + GSM_to + "' ";
            flag = true;
        }

        if (!Piece_No.equals("")) {
            //criteria.add(Restrictions.eq("prPieceNo", Integer.parseInt(Piece_No)));
            
            String[] Pieces = Piece_No.split(",");
            for(int i=0;i<Pieces.length;i++)
            {
                
                if(i==0)
                {
                    //System.out.println("Piece : "+Pieces[i]);
                    SQL = SQL + " AND (PR_PIECE_NO = '"  + Pieces[i] + "' ";
                    //System.out.println("PR_PIECE_NO = '"  + Pieces[i] + "'");
                }
                else
                {
                    //System.out.println("PR_PIECE_NO = '"  + Pieces[i] + "'");
                    SQL = SQL + " OR PR_PIECE_NO = '"  + Pieces[i] + "' ";
                }  
            }
            
            SQL = SQL + ")";
            
            flag = true;
        }

//        if (!Product_Code.equals("")) {
//            // criteria.add(Restrictions.eq("prProductCode", Product_Code));
//            SQL = SQL + " AND PR_PRODUCT_CODE = '" + Product_Code + "' ";
//            flag = true;
//        }
        if (!Product_Code.equals("")) {
            // criteria.add(Restrictions.eq("prProductCode", Product_Code));
            //SQL = SQL + " AND PR_PRODUCT_CODE = '" + Product_Code + "' ";
            
            //Addition using (,)
            String[] Products = Product_Code.split(",");
            for(int i=0;i<Products.length;i++)
            {
                
                if(i==0)
                {
                    //System.out.println("Piece : "+Pieces[i]);
                    SQL = SQL + " AND (PR_PRODUCT_CODE = '"  + Products[i] + "' ";
                    //System.out.println("PR_PIECE_NO = '"  + Pieces[i] + "'");
                }
                else
                {
                    //System.out.println("PR_PIECE_NO = '"  + Pieces[i] + "'");
                    SQL = SQL + " OR PR_PRODUCT_CODE = '"  + Products[i] + "' ";
                }  
            }
            SQL = SQL + ")";
            
            flag = true;
        }

        if (!Zone.equals("All")) {
            // criteria.add(Restrictions.eq("prRegion", Zone));
            SQL = SQL + " AND PR_REGION = '" + Zone + "' ";
            flag = true;
        }

        if (!Incharge.equals("0")) {
            // criteria.add(Restrictions.eq("prIncharge", Incharge));
            SQL = SQL + " AND PR_INCHARGE = '" + Incharge + "' ";
            flag = true;
        }

        if (!MachineNo.equals("")) {
            // criteria.add(Restrictions.eq("prProductCode", Product_Code));
            SQL = SQL + " AND PR_MACHINE_NO = '" + MachineNo + "' ";
            flag = true;
        }

        if (!Position.equals("")) {
            // criteria.add(Restrictions.eq("prProductCode", Product_Code));
            SQL = SQL + " AND PR_POSITION_NO = '" + Position + "' ";
            flag = true;
        }
        
        if (!UPN.equals("")) {
            // criteria.add(Restrictions.eq("prProductCode", Product_Code));
            SQL = SQL + " AND PR_UPN = '" + UPN + "' ";
            flag = true;
        }

        DateFormat df = new SimpleDateFormat("dd/mm/yyyy");
        DateFormat df1 = new SimpleDateFormat("yyyy-mm-dd");
        String fieldname="";
        if (PIECE_STAGE.equals("INVOICED")){
            fieldname="INVOICE";
        }else if (PIECE_STAGE.equals("BSR")){
            fieldname="PACKED";
        }else if (PIECE_STAGE.equals("STOCK")) {
            fieldname = "FNSG";
        }else {
            fieldname="ORDER";
        }
        if (!From_date.equals("") && !To_date.equals("")) {
            //  criteria.add(Restrictions.between("prOrderDate", df.parse(From_date), df.parse(To_date)));
            try {
                
                    SQL = SQL + " AND (PR_"+fieldname+"_DATE BETWEEN '" + df1.format(df.parse(From_date)) + "' AND '" + df1.format(df.parse(To_date)) + "') ";                
                    //SQL = SQL + " AND (PR_ORDER_DATE BETWEEN '" + df1.format(df.parse(From_date)) + "' AND '" + df1.format(df.parse(To_date)) + "') ";
                                
                flag = true;
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (!From_date.equals("") && To_date.equals("")) {
            //  criteria.add(Restrictions.gt("prOrderDate", df.parse(From_date)));
            try {                
                    SQL = SQL + " AND PR_"+fieldname+"_DATE  >= '" + df1.format(df.parse(From_date)) + "' ";                
                    //SQL = SQL + " AND PR_ORDER_DATE  >= '" + df1.format(df.parse(From_date)) + "' ";
                     
                flag = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (From_date.equals("") && !To_date.equals("")) {
            // criteria.add(Restrictions.lt("prOrderDate", df.parse(To_date)));
            try {
                
                    SQL = SQL + " AND PR_"+fieldname+"_DATE  <= '" + df1.format(df.parse(From_date)) + "' ";                
                    //SQL = SQL + " AND PR_ORDER_DATE  <= '" + df1.format(df.parse(From_date)) + "' ";                    
                                
                flag = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (!PIECE_STAGE.equals("")) {
            // criteria.add(Restrictions.eq("prProductCode", Product_Code));
            if (PIECE_STAGE.equals("WIP")) {
                SQL = SQL + " AND PR_PIECE_STAGE IN ('FINISHING','NEEDLING','MENDING','WARPING','WEAVING') ";
            } else if(PIECE_STAGE.equals("STOCK"))
            {
                SQL = SQL + " AND (PR_PIECE_STAGE = 'STOCK' OR PR_PIECE_STAGE = 'IN STOCK') ";
            } else if(PIECE_STAGE.equals("OSG STOCK"))
            {
                SQL = SQL + " AND PR_PIECE_STAGE = 'OSG STOCK' ";
            }
            else if (PIECE_STAGE.equals("DIV_LIST")) {
                SQL = SQL + " AND PR_DIVERSION_FLAG = 'READY' AND PR_PIECE_STAGE NOT IN  ('INVOICED','WEAVING') ";
            } else if (PIECE_STAGE.equals("EXP-INVOICE")) {
                
                SQL = SQL + " AND PR_PIECE_STAGE = 'EXP-INVOICE' ";
                
            } else if (PIECE_STAGE.equals("HOLD")){
                SQL = SQL + "";
            }else {
                SQL = SQL + " AND PR_PIECE_STAGE = '" + PIECE_STAGE + "' ";
            }
            flag = true;
        }
        
        
                
        if (!PIECE_STAGE.equals("")) {
            
            if (PIECE_STAGE.equals("HOLD")) 
            {
                SQL = SQL + " AND PR_PRIORITY_HOLD_CAN_FLAG IN ('6','7','8','10','11','12')";
            }
            else if(PIECE_STAGE.equals("WIP"))
            {
                SQL = SQL + " AND PR_PRIORITY_HOLD_CAN_FLAG IN ('0','1','2','3','4','5')";
            }
             else if(PIECE_STAGE.equals("MENDING"))
            {
                SQL = SQL + " AND PR_PRIORITY_HOLD_CAN_FLAG IN ('0','1','2','3','4','5')";
            }
              else if(PIECE_STAGE.equals("WARPING"))
            {
                SQL = SQL + " AND PR_PRIORITY_HOLD_CAN_FLAG IN ('0','1','2','3','4','5')";
            }
               else if(PIECE_STAGE.equals("WEAVING"))
            {
                SQL = SQL + " AND PR_PRIORITY_HOLD_CAN_FLAG IN ('0','1','2','3','4','5')";
            }
                else if(PIECE_STAGE.equals("NEEDLING"))
            {
                SQL = SQL + " AND PR_PRIORITY_HOLD_CAN_FLAG IN ('0','1','2','3','4','5')";
            }
                 else if(PIECE_STAGE.equals("FINISHING"))
            {
                SQL = SQL + " AND PR_PRIORITY_HOLD_CAN_FLAG IN ('0','1','2','3','4','5')";
            }
                else if(PIECE_STAGE.equals("READY"))
            {
                SQL = SQL + " AND PR_PRIORITY_HOLD_CAN_FLAG IN ('0','1','2','3','4','5')";
            }
                else if(PIECE_STAGE.equals("STOCK"))
            {
                SQL = SQL + " AND PR_PRIORITY_HOLD_CAN_FLAG IN ('0','1','2','3','4','5')";
            }
                else if(PIECE_STAGE.equals("OSG STOCK"))
            {
                SQL = SQL + " AND PR_PRIORITY_HOLD_CAN_FLAG IN ('0','1','2','3','4','5')";
            }
//            else if(PIECE_STAGE.equals("INVOICE"))
//            {
//                SQL = SQL + " AND PR_PRIORITY_HOLD_CAN_FLAG IN ('1','2','3','4','5')";
//            }
            else if(PIECE_STAGE.equals("CANCELED"))
             {
                SQL = SQL + " AND PR_PRIORITY_HOLD_CAN_FLAG IN ('9')";
            }  
            else 
            {
                SQL = SQL + " AND PR_PRIORITY_HOLD_CAN_FLAG IN ('0','1','2','3','4','5') ";
            }
        }        
                
        SQL = SQL + ORDER_BY;
        System.out.println("SQL = " + SQL);

        HashMap hmPieceList = new HashMap();

        try {
            connection = data.getConn();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            resultSet = statement.executeQuery(SQL);
            Ready = true;

            while (resultSet.next()) {
                clsPieceMaster piece = new clsPieceMaster();

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
                
                float Weight = ((Float.parseFloat(resultSet.getString("PR_LENGTH")) * Float.parseFloat(resultSet.getString("PR_WIDTH")) * Float.parseFloat(resultSet.getString("PR_GSM"))) / 1000);

                float SQMT = (Float.parseFloat(resultSet.getString("PR_LENGTH")) * Float.parseFloat(resultSet.getString("PR_WIDTH")));
                
                piece.setAttribute("PR_THORITICAL_WEIGHT", Weight+"");
                piece.setAttribute("PR_SQMTR", SQMT+"");
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

                piece.setAttribute("PR_BILL_LENGTH", resultSet.getString("PR_BILL_LENGTH"));
                piece.setAttribute("PR_BILL_WIDTH", resultSet.getString("PR_BILL_WIDTH"));
                piece.setAttribute("PR_BILL_WEIGHT", resultSet.getString("PR_BILL_WEIGHT"));
                piece.setAttribute("PR_BILL_SQMTR", resultSet.getString("PR_BILL_SQMTR"));
                piece.setAttribute("PR_BILL_GSM", resultSet.getString("PR_BILL_GSM"));
                piece.setAttribute("PR_BILL_PRODUCT_CODE", resultSet.getString("PR_BILL_PRODUCT_CODE"));
                piece.setAttribute("PR_PKG_DP_NO", resultSet.getString("PR_PKG_DP_NO"));
                piece.setAttribute("PR_PKG_DP_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_PKG_DP_DATE")));
                
                piece.setAttribute("PR_UPN", resultSet.getString("PR_UPN"));
                
                hmPieceList.put(hmPieceList.size() + 1, piece);
            }

            return hmPieceList;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Exceoption " + e.getMessage());
            e.printStackTrace();
            LastError = e.getMessage();
            return hmPieceList;
        }
    }
    
   public static HashMap getStatusList() {
        HashMap List= new HashMap();
        try{
            Connection Conn=data.getConn();
            Statement stTmp=Conn.createStatement();
            int counter=1;
            ResultSet rsTmp=stTmp.executeQuery("SELECT PARA_CODE,PARA_DESC FROM PRODUCTION.FELT_PARAMETER_MASTER WHERE PARA_ID='BSR_STATUS' ORDER BY PARA_CODE");
            while(rsTmp.next()){
                ComboData aData=new ComboData();
                aData.Code=rsTmp.getInt("PARA_CODE");
                aData.Text=rsTmp.getString("PARA_DESC");
                List.put(new Integer(counter++), aData);
            }
        }catch(SQLException e){
      //      LastError = e.getMessage();
            e.printStackTrace();
        }
        return List;
    } 
   
   public static HashMap getCobolErpStatusList() {
        HashMap List= new HashMap();
        try{
            Connection Conn=data.getConn();
            Statement stTmp=Conn.createStatement();
            int counter=1;
            ResultSet rsTmp=stTmp.executeQuery("SELECT PARA_CODE,PARA_DESC FROM PRODUCTION.FELT_PARAMETER_MASTER WHERE PARA_ID='COBOL_ERP' ORDER BY PARA_CODE");
            while(rsTmp.next()){
                ComboData aData=new ComboData();
                aData.Code=rsTmp.getInt("PARA_CODE");
                aData.Text=rsTmp.getString("PARA_DESC");
                List.put(new Integer(counter++), aData);
            }
        }catch(SQLException e){
      //      LastError = e.getMessage();
            e.printStackTrace();
        }
        return List;
    } 

}
