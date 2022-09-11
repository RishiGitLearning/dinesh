/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales.common;

import EITLERP.EITLERPGLOBAL;
import EITLERP.FeltSales.PieceAmendmentApproval_STOCK.clsPieceAmendApprovalDetail_STOCK;
import EITLERP.FeltSales.PieceAmendmentApproval_STOCK.clsPieceAmendApproval_STOCK;
import EITLERP.FeltSales.PieceAmendmentWIP.clsPieceAmendWIP;
import EITLERP.FeltSales.PieceAmendmentWIP.clsPieceAmendWIPDetail;
import EITLERP.clsFirstFree;
import EITLERP.clsSales_Party;
import EITLERP.clsUser;
import EITLERP.data;
import java.util.Date;
import java.util.HashMap;

/**
 *
 * @author root
 */
public class clsWIP_STOCK_Review {
 
    public static void main(String args[])
    {
    
        String Monnt_name = data.getStringValueFromDB("SELECT MONTHNAME(subdate(curdate(),15))");
        String Year = data.getStringValueFromDB("SELECT YEAR(subdate(curdate(),15))");
        
        Date date = new Date();
        
        String Expected_Month = Monnt_name.substring(0, 3) + " - " + Year;   
        System.out.println("EXPECTED MONTH = "+Expected_Month);       
        
        
        if(date.getDate()==1)
        {
        
    //  UNCOMMENT THIS COMMENT CODE TO RUN AUTO ENTRY ON EXPECTED DISPATCH DATE 31-Oct-2018
       
        // String Expected_Month = "Dec - 2018"; - Run on 03/Jan/2019
        //String Expected_Month = "Jan - 2019"; - Run on 01/Feb/2019
        //String Expected_Month = "Feb - 2019"; 
        //String Expected_Month = "Apr - 2019"; // - Run on 08/May/2019 
        //String Expected_Month = "May - 2019"; // - Run on 01/June/2019 
               
        
               
        clsPieceAmendWIP objPieceAmendApproval =new clsPieceAmendWIP();
        HashMap hmPieceList = objPieceAmendApproval.getPieceList(Expected_Month);
        //System.out.println("SIXE "+hmPieceList.size());
        
        if(hmPieceList.size()>0)
        {
                
                    //======= Set Line part ============
                    try {
                        objPieceAmendApproval.hmPieceAmendApprovalDetail.clear();
                        int created_by=0;
                       for(int j=1;j<=hmPieceList.size();j++)
                        {
                                clsPieceAmendWIPDetail objFeltOrderDetails = new clsPieceAmendWIPDetail();
                                 clsPieceAmendWIP piece = (clsPieceAmendWIP) hmPieceList.get(j);

                                String DOC_NO1 = clsFirstFree.getNextFreeNo(EITLERPGLOBAL.gCompanyID, 774, 249, true); 

                                objPieceAmendApproval.setAttribute("PIECE_AMEND_NO", DOC_NO1);
                                objPieceAmendApproval.setAttribute("PIECE_AMEND_DATE", EITLERPGLOBAL.getCurrentDateDB());


                                objPieceAmendApproval.setAttribute("DOC_NO", DOC_NO1);
                                objPieceAmendApproval.setAttribute("DOC_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                objPieceAmendApproval.setAttribute("MODULE_ID", 774);


                                //----- Update Approval Specific Fields -----------//

                                objPieceAmendApproval.setAttribute("FROM_REMARKS", "");
                                objPieceAmendApproval.setAttribute("APPROVER_REMARKS", "");
                                objPieceAmendApproval.setAttribute("REJECTED_REMARKS", "");

                                objPieceAmendApproval.setAttribute("APPROVAL_STATUS", "H");
                                objPieceAmendApproval.setAttribute("ENTRY_REASON", "AUTO_EXPECTED_MONTH");
                                objPieceAmendApproval.setAttribute("ENTRY_DOCNO", "");    
                                objPieceAmendApproval.setAttribute("CREATED_DATE", EITLERPGLOBAL.getCurrentDateTimeDB());

                                created_by = data.getIntValueFromDB("SELECT CREATED_BY FROM PRODUCTION.FELT_SALES_PIECE_AMENDMENT_HEADER_WIP WHERE MM_PARTY_CODE='"+(String)piece.getAttribute("MM_PARTY_CODE").getObj()+"' AND MM_MACHINE_NO="+(String)piece.getAttribute("MM_MACHINE_NO").getObj()+" AND MM_MACHINE_POSITION="+(String)piece.getAttribute("MM_MACHINE_POSITION").getObj()+" ORDER BY PIECE_AMEND_NO DESC");
                                 
                                        objFeltOrderDetails.setAttribute("PIECE_AMEND_NO",DOC_NO1);

                                        objFeltOrderDetails.setAttribute("PIECE_NO",(String)piece.getAttribute("PIECE_NO").getObj());
//                                      objFeltOrderDetails.setAttribute("MM_PARTY_CODE", Party_Code);
//                                      objFeltOrderDetails.setAttribute("MM_MACHINE_NO",  MachineNo);
//                                      objFeltOrderDetails.setAttribute("MM_MACHINE_POSITION",  Position);

                                        objPieceAmendApproval.setAttribute("MM_PARTY_CODE", (String)piece.getAttribute("MM_PARTY_CODE").getObj());
                                        objPieceAmendApproval.setAttribute("MM_MACHINE_NO", (String)piece.getAttribute("MM_MACHINE_NO").getObj());
                                        objPieceAmendApproval.setAttribute("MM_MACHINE_POSITION", (String)piece.getAttribute("MM_MACHINE_POSITION").getObj());

                                        objFeltOrderDetails.setAttribute("CHANGE_POSIBILITY",  0);
                                        objFeltOrderDetails.setAttribute("DELINK",  0);
                                        objFeltOrderDetails.setAttribute("ACTUAL_CHANGE",  0);
                                        objFeltOrderDetails.setAttribute("PROD_REMARKS",  "");
                                        objFeltOrderDetails.setAttribute("ACTION_TAKEN",  "");
                                        objFeltOrderDetails.setAttribute("REMARKS",  "");	

                                        objFeltOrderDetails.setAttribute("LENGTH", (String)piece.getAttribute("LENGTH").getObj());
                                        objFeltOrderDetails.setAttribute("LENGTH_UPDATED", (String)piece.getAttribute("LENGTH_UPDATED").getObj());
                                        objFeltOrderDetails.setAttribute("WIDTH", (String)piece.getAttribute("WIDTH").getObj());
                                        objFeltOrderDetails.setAttribute("WIDTH_UPDATED",  (String)piece.getAttribute("WIDTH_UPDATED").getObj());
                                        objFeltOrderDetails.setAttribute("GSM",  (String)piece.getAttribute("GSM").getObj());
                                        objFeltOrderDetails.setAttribute("GSM_UPDATED",  (String)piece.getAttribute("GSM_UPDATED").getObj());
                                        objFeltOrderDetails.setAttribute("STYLE", (String)piece.getAttribute("STYLE").getObj());
                                        objFeltOrderDetails.setAttribute("STYLE_UPDATED",  (String)piece.getAttribute("STYLE_UPDATED").getObj());
                                        objFeltOrderDetails.setAttribute("PRODUCTCODE", (String)piece.getAttribute("PRODUCTCODE").getObj());
                                        objFeltOrderDetails.setAttribute("PRODUCTCODE_UPDATED",  (String)piece.getAttribute("PRODUCTCODE_UPDATED").getObj());
                                        objFeltOrderDetails.setAttribute("FLET_GROUP",  (String)piece.getAttribute("FLET_GROUP").getObj());
                                        objFeltOrderDetails.setAttribute("FLET_GROUP_UPDATED",  (String)piece.getAttribute("FLET_GROUP_UPDATED").getObj());

                                        float l = Float.parseFloat((String)piece.getAttribute("LENGTH").getObj());
                                        float l_u = Float.parseFloat((String)piece.getAttribute("LENGTH_UPDATED").getObj());

                                        float w = Float.parseFloat((String)piece.getAttribute("WIDTH").getObj());
                                        float w_u = Float.parseFloat((String)piece.getAttribute("WIDTH_UPDATED").getObj());

                                        float g = Float.parseFloat((String)piece.getAttribute("GSM").getObj());
                                        float g_u = Float.parseFloat((String)piece.getAttribute("GSM_UPDATED").getObj());

                                        objFeltOrderDetails.setAttribute("WEIGHT",  ((l*w*g)/1000)+"");
                                        objFeltOrderDetails.setAttribute("WEIGHT_UPDATED", ((l_u*w_u*g_u)/1000)+"");
                                        objFeltOrderDetails.setAttribute("SQMTR", EITLERPGLOBAL.round((l*w),2)+"");
                                        objFeltOrderDetails.setAttribute("SQMTR_UPDATED", EITLERPGLOBAL.round((l_u*w_u),2)+"");
                                        objFeltOrderDetails.setAttribute("PIECE_STAGE", (String)piece.getAttribute("PIECE_STAGE").getObj());
                                        objFeltOrderDetails.setAttribute("EXPECTED_DISPATCH", Expected_Month);

//                                                                        objFeltOrderDetails.setAttribute("UNTICK_USER", "");
//                                                                        objFeltOrderDetails.setAttribute("UNTICK_REMARK_DESIGN", "");
//                                                                        objFeltOrderDetails.setAttribute("UNTICK_REMARK_PRODUCTION", "");
//                                                                        objFeltOrderDetails.setAttribute("ACTION_TAKEN", "CHANGE SPECIFICATION");

                                        objPieceAmendApproval.hmPieceAmendApprovalDetail.put(Integer.toString(objPieceAmendApproval.hmPieceAmendApprovalDetail.size() + 1), objFeltOrderDetails);
                                        objPieceAmendApproval.setAttribute("FROM", 0);
                                        objPieceAmendApproval.setAttribute("TO", 0);
                                        objPieceAmendApproval.setAttribute("USER_ID", 0);
                                        objPieceAmendApproval.setAttribute("HIERARCHY_ID",0);
                                        objPieceAmendApproval.setAttribute("CREATED_BY", 0);
                                        
                                        objPieceAmendApproval.setAttribute("FROM", 26);
                                        objPieceAmendApproval.setAttribute("TO", 26);
                                        objPieceAmendApproval.setAttribute("USER_ID", 26);
                                        objPieceAmendApproval.setAttribute("HIERARCHY_ID",3707);
                                        objPieceAmendApproval.setAttribute("CREATED_BY", 26);
                                        

                                       if(objPieceAmendApproval.Insert())
                                       {

                                       }
                             }
                             
                    } catch (Exception e) {

                        System.out.println("Eroor on setData : "+e.getMessage());
                        e.printStackTrace();
                    };


            }
  
                        
            //IN STOCK ENTRY
    
            clsPieceAmendApproval_STOCK ObjStock =new clsPieceAmendApproval_STOCK();
            HashMap hmPieceList_Stock = ObjStock.getPieceList_STOCK_Reentry(Expected_Month);

            if(hmPieceList_Stock.size()>0)
            {
                
                    //======= Set Line part ============
                    try {
                        
                        int created_by=0;
                       for(int j=1;j<=hmPieceList_Stock.size();j++)
                        {
                            ObjStock.hmPieceAmendApprovalDetail.clear();
                                clsPieceAmendApprovalDetail_STOCK objFeltOrderDetails = new clsPieceAmendApprovalDetail_STOCK();
                                clsPieceAmendApproval_STOCK piece = (clsPieceAmendApproval_STOCK) hmPieceList_Stock.get(j);

                                String DOC_NO1 = clsFirstFree.getNextFreeNo(EITLERPGLOBAL.gCompanyID, 763, 227, true); 

                                ObjStock.setAttribute("PIECE_AMEND_STOCK_NO", DOC_NO1);
                                ObjStock.setAttribute("PIECE_AMEND_DATE", EITLERPGLOBAL.getCurrentDateDB());

                                ObjStock.setAttribute("DOC_NO", DOC_NO1);
                                ObjStock.setAttribute("DOC_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                ObjStock.setAttribute("MODULE_ID", 774);

                                //----- Update Approval Specific Fields -----------//

                                ObjStock.setAttribute("FROM_REMARKS", "");
                                ObjStock.setAttribute("APPROVER_REMARKS", "");
                                ObjStock.setAttribute("REJECTED_REMARKS", "");

                                ObjStock.setAttribute("APPROVAL_STATUS", "H");
                                ObjStock.setAttribute("ENTRY_REASON", "AUTO_EXPECTED_MONTH");
                                ObjStock.setAttribute("ENTRY_DOCNO", "");
                                        
                                ObjStock.setAttribute("CREATED_DATE", EITLERPGLOBAL.getCurrentDateTimeDB());

                                created_by = data.getIntValueFromDB("SELECT CREATED_BY FROM PRODUCTION.FELT_SALES_PIECE_AMEND_APPROVAL_STOCK_HEADER WHERE MM_PARTY_CODE='"+(String)piece.getAttribute("MM_PARTY_CODE").getObj()+"' AND MM_MACHINE_NO="+(String)piece.getAttribute("MM_MACHINE_NO").getObj()+" AND MM_MACHINE_POSITION="+(String)piece.getAttribute("MM_MACHINE_POSITION").getObj()+" ORDER BY PIECE_AMEND_STOCK_NO DESC");
                                 
                                        objFeltOrderDetails.setAttribute("PIECE_AMEND_STOCK_NO",DOC_NO1);

                                        objFeltOrderDetails.setAttribute("PIECE_NO",(String)piece.getAttribute("PIECE_NO").getObj());
//                                      objFeltOrderDetails.setAttribute("MM_PARTY_CODE", Party_Code);
//                                      objFeltOrderDetails.setAttribute("MM_MACHINE_NO",  MachineNo);
//                                      objFeltOrderDetails.setAttribute("MM_MACHINE_POSITION",  Position);

                                        ObjStock.setAttribute("MM_PARTY_CODE", (String)piece.getAttribute("MM_PARTY_CODE").getObj());
                                        ObjStock.setAttribute("MM_MACHINE_NO", (String)piece.getAttribute("MM_MACHINE_NO").getObj());
                                        ObjStock.setAttribute("MM_MACHINE_POSITION", (String)piece.getAttribute("MM_MACHINE_POSITION").getObj());

                                        objFeltOrderDetails.setAttribute("SELECTED",  0);
                                        objFeltOrderDetails.setAttribute("REMARKS",  "");	

                                        objFeltOrderDetails.setAttribute("LENGTH", (String)piece.getAttribute("LENGTH").getObj());
                                        objFeltOrderDetails.setAttribute("LENGTH_UPDATED", (String)piece.getAttribute("LENGTH_UPDATED").getObj());
                                        objFeltOrderDetails.setAttribute("WIDTH", (String)piece.getAttribute("WIDTH").getObj());
                                        objFeltOrderDetails.setAttribute("WIDTH_UPDATED",  (String)piece.getAttribute("WIDTH_UPDATED").getObj());
                                        objFeltOrderDetails.setAttribute("GSM",  (String)piece.getAttribute("GSM").getObj());
                                        objFeltOrderDetails.setAttribute("GSM_UPDATED",  (String)piece.getAttribute("GSM_UPDATED").getObj());
                                        objFeltOrderDetails.setAttribute("STYLE", (String)piece.getAttribute("STYLE").getObj());
                                        objFeltOrderDetails.setAttribute("STYLE_UPDATED",  (String)piece.getAttribute("STYLE_UPDATED").getObj());
                                        objFeltOrderDetails.setAttribute("PRODUCTCODE", (String)piece.getAttribute("PRODUCTCODE").getObj());
                                        objFeltOrderDetails.setAttribute("PRODUCTCODE_UPDATED",  (String)piece.getAttribute("PRODUCTCODE_UPDATED").getObj());
                                        objFeltOrderDetails.setAttribute("FLET_GROUP",  (String)piece.getAttribute("FLET_GROUP").getObj());
                                        objFeltOrderDetails.setAttribute("FLET_GROUP_UPDATED",  (String)piece.getAttribute("FLET_GROUP_UPDATED").getObj());

                                        float l = Float.parseFloat((String)piece.getAttribute("LENGTH").getObj());
                                        float l_u = Float.parseFloat((String)piece.getAttribute("LENGTH_UPDATED").getObj());

                                        float w = Float.parseFloat((String)piece.getAttribute("WIDTH").getObj());
                                        float w_u = Float.parseFloat((String)piece.getAttribute("WIDTH_UPDATED").getObj());

                                        float g = Float.parseFloat((String)piece.getAttribute("GSM").getObj());
                                        float g_u = Float.parseFloat((String)piece.getAttribute("GSM_UPDATED").getObj());

                                        objFeltOrderDetails.setAttribute("WEIGHT",  ((l*w*g)/1000)+"");
                                        objFeltOrderDetails.setAttribute("WEIGHT_UPDATED", ((l_u*w_u*g_u)/1000)+"");
                                        objFeltOrderDetails.setAttribute("SQMTR", EITLERPGLOBAL.round((l*w),2)+"");
                                        objFeltOrderDetails.setAttribute("SQMTR_UPDATED", EITLERPGLOBAL.round((l_u*w_u),2)+"");
                                        objFeltOrderDetails.setAttribute("PIECE_STAGE", (String)piece.getAttribute("PIECE_STAGE").getObj());
                                        objFeltOrderDetails.setAttribute("EXPECTED_DISPATCH", Expected_Month);

//                                                                        objFeltOrderDetails.setAttribute("UNTICK_USER", "");
//                                                                        objFeltOrderDetails.setAttribute("UNTICK_REMARK_DESIGN", "");
//                                                                        objFeltOrderDetails.setAttribute("UNTICK_REMARK_PRODUCTION", "");
//                                                                        objFeltOrderDetails.setAttribute("ACTION_TAKEN", "CHANGE SPECIFICATION");

                                        ObjStock.hmPieceAmendApprovalDetail.put(Integer.toString(ObjStock.hmPieceAmendApprovalDetail.size() + 1), objFeltOrderDetails);
                                        
                                        ObjStock.setAttribute("FROM", 26);
                                        ObjStock.setAttribute("TO", 26);
                                        ObjStock.setAttribute("USER_ID", 26);
                                        ObjStock.setAttribute("CREATED_BY", 26);
                                        ObjStock.setAttribute("HIERARCHY_ID", 3708);
                                        
                                               
                                       if(ObjStock.Insert())
                                       {

                                       }
                             }
                             
                    } catch (Exception e) {

                        System.out.println("Eroor on setData : "+e.getMessage());
                        e.printStackTrace();
                    };
            }
        }
    }
            
}
    
    
    
    
    


