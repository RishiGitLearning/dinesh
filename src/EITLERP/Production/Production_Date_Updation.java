/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.Production;

import java.sql.Connection;
import java.sql.Statement;
import static sdml.felt.commonUI.data.getConn;

/**
 *
 * @author Dharmendra
 */
public class Production_Date_Updation {

    String sql;
    Connection Conn;
    Statement stmt;

    public Production_Date_Updation() {
        try {
            Conn = getConn();
            stmt = Conn.createStatement();

            UpdateWarping();
            UpdateWeaving();
            UpdateMending();
            UpdateNeedling();
            UpdateSeaming();
            UpdateFinishing();
            //UpdateHeatSetting();

            sql = "UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_PIECE_STAGE='WEAVING',WIP_STATUS='WARPED' "
                    + "WHERE COALESCE(WIP_WARP_DATE,'0000-00-00')!='0000-00-00' "
                    + "AND COALESCE(WIP_WVG_DATE,'0000-00-00')='0000-00-00'  "
                    + "AND COALESCE(WIP_MND_DATE,'0000-00-00')='0000-00-00'  "
                    + "AND COALESCE(WIP_HEATSET_DATE,'0000-00-00')='0000-00-00'  "
                    + "AND COALESCE(WIP_NDL_DATE,'0000-00-00')='0000-00-00'  "
                    + "AND COALESCE(WIP_SEAM_DATE,'0000-00-00')='0000-00-00'  "
                    + "AND COALESCE(WIP_FNSG_DATE,'0000-00-00')='0000-00-00'  "
                    + "AND WIP_PIECE_STAGE IN ('BOOKING','PLANNING','WEAVING','MENDING','HEATSETTING','NEEDLING','FINISHING','SEAMING') "
                    + "AND COALESCE(WIP_REJECTED_FLAG,0)=0";
            System.out.println("WarpUpdate:" + sql);
            stmt.execute(sql);
            
            
            sql = "UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_PIECE_STAGE='MENDING',WIP_STATUS='WOVEN' "
                    + "WHERE COALESCE(WIP_WARP_DATE,'0000-00-00')!='0000-00-00' "
                    + "AND COALESCE(WIP_WVG_DATE,'0000-00-00')!='0000-00-00'  "
                    + "AND COALESCE(WIP_MND_DATE,'0000-00-00')='0000-00-00'  "
                    + "AND COALESCE(WIP_HEATSET_DATE,'0000-00-00')='0000-00-00'  "
                    + "AND COALESCE(WIP_NDL_DATE,'0000-00-00')='0000-00-00'  "
                    + "AND COALESCE(WIP_SEAM_DATE,'0000-00-00')='0000-00-00'  "
                    + "AND COALESCE(WIP_FNSG_DATE,'0000-00-00')='0000-00-00'  "
                    + "AND WIP_STYLE NOT IN ('Y 18271-5','Y 18272-5') "
                    + "AND WIP_PIECE_STAGE IN ('BOOKING','PLANNING','WEAVING','MENDING','HEATSETTING','NEEDLING','FINISHING','SEAMING') "
                    + "AND COALESCE(WIP_REJECTED_FLAG,0)=0";
            System.out.println("WeaveUpdate:" + sql);
            stmt.execute(sql);
            
            /*
            sql = "UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_PIECE_STAGE='NEEDLING',WIP_STATUS='MENDED' "
                    + "WHERE COALESCE(WIP_WARP_DATE,'0000-00-00')!='0000-00-00' "
                    + "AND COALESCE(WIP_WVG_DATE,'0000-00-00')!='0000-00-00'  "
                    + "AND COALESCE(WIP_MND_DATE,'0000-00-00')!='0000-00-00'  "
                    + "AND COALESCE(WIP_NDL_DATE,'0000-00-00')='0000-00-00'  "
                    + "AND COALESCE(WIP_SEAM_DATE,'0000-00-00')='0000-00-00'  "
                    + "AND COALESCE(WIP_FNSG_DATE,'0000-00-00')='0000-00-00'  "
                    + "AND WIP_PIECE_STAGE IN ('BOOKING','PLANNING','WEAVING','MENDING','NEEDLING','FINISHING','SEAMING') "
                    + "AND WIP_GROUP NOT IN ('HDS','M50','M35')"
                    + "AND COALESCE(WIP_REJECTED_FLAG,0)=0";
            System.out.println("MendUpdate:" + sql);
            stmt.execute(sql);
            
            sql = "UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_PIECE_STAGE='SEAMING',WIP_STATUS='MENDED' "
                    + "WHERE COALESCE(WIP_WARP_DATE,'0000-00-00')!='0000-00-00' "
                    + "AND COALESCE(WIP_WVG_DATE,'0000-00-00')!='0000-00-00'  "
                    + "AND COALESCE(WIP_MND_DATE,'0000-00-00')!='0000-00-00'  "
                    + "AND COALESCE(WIP_NDL_DATE,'0000-00-00')='0000-00-00'  "
                    + "AND COALESCE(WIP_SEAM_DATE,'0000-00-00')='0000-00-00'  "
                    + "AND COALESCE(WIP_FNSG_DATE,'0000-00-00')='0000-00-00'  "
                    + "AND WIP_PIECE_STAGE IN ('BOOKING','PLANNING','WEAVING','MENDING','NEEDLING','FINISHING','SEAMING') "
                    + "AND WIP_GROUP IN ('HDS','M50','M35')"
                    + "AND COALESCE(WIP_REJECTED_FLAG,0)=0";
            System.out.println("MendUpdate:" + sql);
            stmt.execute(sql);
             */
            sql = "UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_PIECE_STAGE='FINISHING',WIP_STATUS='NEEDLED' "
                    + "WHERE COALESCE(WIP_WARP_DATE,'0000-00-00')!='0000-00-00' "
                    + "AND COALESCE(WIP_WVG_DATE,'0000-00-00')!='0000-00-00'  "
                    + "AND COALESCE(WIP_MND_DATE,'0000-00-00')!='0000-00-00'  "
                    + "AND COALESCE(WIP_NDL_DATE,'0000-00-00')!='0000-00-00'  "
                    + "AND COALESCE(WIP_SEAM_DATE,'0000-00-00')='0000-00-00'  "
                    + "AND COALESCE(WIP_FNSG_DATE,'0000-00-00')='0000-00-00'  "
                    + "AND WIP_PIECE_STAGE IN ('BOOKING','PLANNING','WEAVING','MENDING','NEEDLING','FINISHING','SEAMING') "
                    + "AND COALESCE(WIP_REJECTED_FLAG,0)=0";
            System.out.println("MendUpdate:" + sql);
            stmt.execute(sql);

            sql = "UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_PIECE_STAGE='FINISHING',WIP_STATUS='SEAMED' "
                    + "WHERE COALESCE(WIP_WARP_DATE,'0000-00-00')!='0000-00-00' "
                    + "AND COALESCE(WIP_WVG_DATE,'0000-00-00')!='0000-00-00'  "
                    + "AND COALESCE(WIP_MND_DATE,'0000-00-00')!='0000-00-00'  "
                    + "AND COALESCE(WIP_NDL_DATE,'0000-00-00')='0000-00-00'  "
                    + "AND COALESCE(WIP_SEAM_DATE,'0000-00-00')!='0000-00-00'  "
                    + "AND COALESCE(WIP_FNSG_DATE,'0000-00-00')='0000-00-00'  "
                    + "AND WIP_PIECE_STAGE IN ('BOOKING','PLANNING','WEAVING','MENDING','NEEDLING','FINISHING','SEAMING') "
                    + "AND COALESCE(WIP_REJECTED_FLAG,0)=0";
            System.out.println("SeamUpdate:" + sql);
            stmt.execute(sql);

            sql = "UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_PIECE_STAGE='IN STOCK',WIP_STATUS='FINISHED' "
                    + "WHERE COALESCE(WIP_WARP_DATE,'0000-00-00')!='0000-00-00' "
                    + "AND COALESCE(WIP_WVG_DATE,'0000-00-00')!='0000-00-00'  "
                    + "AND COALESCE(WIP_MND_DATE,'0000-00-00')!='0000-00-00'  "
                    + "AND (COALESCE(WIP_NDL_DATE,'0000-00-00')!='0000-00-00'  "
                    + "OR COALESCE(WIP_SEAM_DATE,'0000-00-00')!='0000-00-00')  "
                    + "AND COALESCE(WIP_FNSG_DATE,'0000-00-00')!='0000-00-00'  "
                    + "AND WIP_PIECE_STAGE IN ('BOOKING','PLANNING','WEAVING','MENDING','NEEDLING','FINISHING','SEAMING') "
                    + "AND COALESCE(WIP_REJECTED_FLAG,0)=0";
            System.out.println("FinishUpdate:" + sql);
            stmt.execute(sql);

            sql = "UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER R,PRODUCTION.FELT_WIP_PIECE_REGISTER W "
                    + "SET R.PR_PIECE_STAGE=W.WIP_PIECE_STAGE,R.PR_WIP_STATUS=W.WIP_STATUS,"
                    + "R.PR_WARP_DATE=W.WIP_WARP_DATE,R.PR_WARP_A_DATE=W.WIP_WARP_A_DATE "
                    + "WHERE R.PR_PIECE_NO=W.WIP_PIECE_NO "
                    + "AND W.WIP_PIECE_STAGE='WEAVING' "
                    //+ "AND W.WIP_PIECE_STAGE!=R.PR_PIECE_STAGE "
                    + "AND PR_PIECE_STAGE IN ('BOOKING','PLANNING','WEAVING','MENDING','NEEDLING','FINISHING','SEAMING') "
                    + "AND WIP_EXT_PIECE_NO NOT LIKE '%-AB' "
                    + "AND WIP_EXT_PIECE_NO NOT LIKE '%B'";
            System.out.println("SalePRWeaving[Without B]:" + sql);
            stmt.execute(sql);
            sql = "UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER R,PRODUCTION.FELT_WIP_PIECE_REGISTER W "
                    + "SET R.PR_PIECE_STAGE=W.WIP_PIECE_STAGE,R.PR_WIP_STATUS=W.WIP_STATUS,"
                    + "R.PR_WARP_DATE=W.WIP_WARP_DATE,R.PR_WARP_B_DATE=W.WIP_WARP_B_DATE "
                    + "WHERE R.PR_PIECE_NO=W.WIP_PIECE_NO "
                    + "AND W.WIP_PIECE_STAGE='WEAVING' "
                    + "AND PR_PIECE_STAGE IN ('BOOKING','PLANNING','WEAVING','MENDING','NEEDLING','FINISHING','SEAMING') "
                    //+ "AND W.WIP_PIECE_STAGE!=R.PR_PIECE_STAGE "
                    + "AND WIP_EXT_PIECE_NO NOT LIKE '%-AB' "
                    + "AND WIP_EXT_PIECE_NO LIKE '%B'";
            System.out.println("SalePRWeaving[With B]:" + sql);
            stmt.execute(sql);

            sql = "UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER R,PRODUCTION.FELT_WIP_PIECE_REGISTER W "
                    + "SET R.PR_PIECE_STAGE=W.WIP_PIECE_STAGE,R.PR_WIP_STATUS=W.WIP_STATUS,"
                    + "R.PR_WVG_DATE=W.WIP_WVG_DATE,R.PR_WVG_A_DATE=W.WIP_WVG_A_DATE "
                    + "WHERE R.PR_PIECE_NO=W.WIP_PIECE_NO "
                    + "AND W.WIP_PIECE_STAGE='MENDING' "
                    //+ "AND W.WIP_PIECE_STAGE!=R.PR_PIECE_STAGE "
                    + "AND PR_PIECE_STAGE IN ('BOOKING','PLANNING','WEAVING','MENDING','NEEDLING','FINISHING','SEAMING') "
                    + "AND WIP_EXT_PIECE_NO NOT LIKE '%-AB' "
                    + "AND WIP_EXT_PIECE_NO NOT LIKE '%B'";
            System.out.println("SalePRMending[Without B]:" + sql);
            stmt.execute(sql);
            sql = "UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER R,PRODUCTION.FELT_WIP_PIECE_REGISTER W "
                    + "SET R.PR_PIECE_STAGE=W.WIP_PIECE_STAGE,R.PR_WIP_STATUS=W.WIP_STATUS,"
                    + "R.PR_WVG_DATE=W.WIP_WVG_DATE,R.PR_WVG_B_DATE=W.WIP_WVG_B_DATE "
                    + "WHERE R.PR_PIECE_NO=W.WIP_PIECE_NO "
                    + "AND W.WIP_PIECE_STAGE='MENDING' "
                    + "AND PR_PIECE_STAGE IN ('BOOKING','PLANNING','WEAVING','MENDING','NEEDLING','FINISHING','SEAMING') "
                    //+ "AND W.WIP_PIECE_STAGE!=R.PR_PIECE_STAGE "
                    + "AND WIP_EXT_PIECE_NO NOT LIKE '%-AB' "
                    + "AND WIP_EXT_PIECE_NO LIKE '%B'";
            System.out.println("SalePRMending[With B]:" + sql);
            stmt.execute(sql);

            sql = "UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER R,PRODUCTION.FELT_WIP_PIECE_REGISTER W "
                    + "SET R.PR_PIECE_STAGE=W.WIP_PIECE_STAGE,R.PR_WIP_STATUS=W.WIP_STATUS,"
                    + "R.PR_MND_DATE=W.WIP_MND_DATE,R.PR_MND_A_DATE=W.WIP_MND_A_DATE "
                    + "WHERE R.PR_PIECE_NO=W.WIP_PIECE_NO "
                    + "AND W.WIP_PIECE_STAGE='NEEDLING' "
                    //+ "AND W.WIP_PIECE_STAGE!=R.PR_PIECE_STAGE "
                    + "AND PR_PIECE_STAGE IN ('BOOKING','PLANNING','WEAVING','MENDING','NEEDLING','FINISHING','SEAMING') "
                    //+ "AND W.WIP_PIECE_STAGE!=R.PR_PIECE_STAGE "
                    
                    + "AND WIP_EXT_PIECE_NO NOT LIKE '%-AB' "
                    + "AND WIP_EXT_PIECE_NO NOT LIKE '%B'";
            System.out.println("SalePRWarpNeedling:" + sql);
            stmt.execute(sql);

            sql = "UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER R,PRODUCTION.FELT_WIP_PIECE_REGISTER W "
                    + "SET R.PR_PIECE_STAGE=W.WIP_PIECE_STAGE,R.PR_WIP_STATUS=W.WIP_STATUS,"
                    + "R.PR_MND_DATE=W.WIP_MND_DATE,R.PR_MND_B_DATE=W.WIP_MND_B_DATE "
                    + "WHERE R.PR_PIECE_NO=W.WIP_PIECE_NO "
                    + "AND W.WIP_PIECE_STAGE='NEEDLING' "
                    //+ "AND W.WIP_PIECE_STAGE!=R.PR_PIECE_STAGE "
                    + "AND PR_PIECE_STAGE IN ('BOOKING','PLANNING','WEAVING','MENDING','NEEDLING','FINISHING','SEAMING') "
                    //+ "AND W.WIP_PIECE_STAGE!=R.PR_PIECE_STAGE "
                    + "AND WIP_EXT_PIECE_NO NOT LIKE '%-AB' "
                    + "AND WIP_EXT_PIECE_NO LIKE '%B'";
            System.out.println("SalePRWarpNeedling:" + sql);
            stmt.execute(sql);

            sql = "UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER R,PRODUCTION.FELT_WIP_PIECE_REGISTER W "
                    + "SET R.PR_PIECE_STAGE=W.WIP_PIECE_STAGE,R.PR_WIP_STATUS=W.WIP_STATUS,"
                    + "R.PR_SEAM_DATE=W.WIP_SEAM_DATE "
                    + "WHERE R.PR_PIECE_NO=W.WIP_PIECE_NO "
                    + "AND W.WIP_PIECE_STAGE='SEAMING' "
                    //+ "AND W.WIP_PIECE_STAGE!=R.PR_PIECE_STAGE "
                    + "AND PR_PIECE_STAGE IN ('BOOKING','PLANNING','WEAVING','MENDING','NEEDLING','FINISHING','SEAMING') "
                    //+ "AND W.WIP_PIECE_STAGE!=R.PR_PIECE_STAGE "
                    + "AND WIP_EXT_PIECE_NO NOT LIKE '%-AB' "
                    + "AND WIP_EXT_PIECE_NO NOT LIKE '%B'";
            System.out.println("SalePRWarpSeaming:" + sql);
            stmt.execute(sql);

            sql = "UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER R,PRODUCTION.FELT_WIP_PIECE_REGISTER W "
                    + "SET R.PR_PIECE_STAGE=W.WIP_PIECE_STAGE,R.PR_WIP_STATUS=W.WIP_STATUS,"
                    + "R.PR_SEAM_DATE=W.WIP_SEAM_DATE "
                    + "WHERE R.PR_PIECE_NO=W.WIP_PIECE_NO "
                    + "AND W.WIP_PIECE_STAGE='SEAMING' "
                    //+ "AND W.WIP_PIECE_STAGE!=R.PR_PIECE_STAGE "
                    + "AND PR_PIECE_STAGE IN ('BOOKING','PLANNING','WEAVING','MENDING','NEEDLING','FINISHING','SEAMING') "
                    + "AND WIP_EXT_PIECE_NO NOT LIKE '%-AB' "
                    + "AND WIP_EXT_PIECE_NO  LIKE '%B'";
            System.out.println("SalePRWarpSeaming:" + sql);
            stmt.execute(sql);

            sql = "UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER R,PRODUCTION.FELT_WIP_PIECE_REGISTER W "
                    + "SET R.PR_PIECE_STAGE=W.WIP_PIECE_STAGE,R.PR_WIP_STATUS=W.WIP_STATUS,"
                    + "R.PR_SEAM_DATE=W.WIP_SEAM_DATE,R.PR_NDL_DATE=W.WIP_NDL_DATE "
                    + "WHERE R.PR_PIECE_NO=W.WIP_PIECE_NO "
                    + "AND W.WIP_PIECE_STAGE='FINISHING' "
                    //+ "AND W.WIP_PIECE_STAGE!=R.PR_PIECE_STAGE "
                    + "AND PR_PIECE_STAGE IN ('BOOKING','PLANNING','WEAVING','MENDING','NEEDLING','FINISHING','SEAMING') ";
            System.out.println("SalePRFinishing:" + sql);
            stmt.execute(sql);

            sql = "UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER R,PRODUCTION.FELT_WIP_PIECE_REGISTER W "
                    + "SET R.PR_PIECE_STAGE=W.WIP_PIECE_STAGE,R.PR_WIP_STATUS=W.WIP_STATUS,"
                    + "R.PR_FNSG_DATE=W.WIP_FNSG_DATE "
                    + "WHERE R.PR_PIECE_NO=W.WIP_PIECE_NO "
                    + "AND W.WIP_PIECE_STAGE='IN STOCK' "
                    //+ "AND W.WIP_PIECE_STAGE!=R.PR_PIECE_STAGE "
                    + "AND PR_PIECE_STAGE IN ('BOOKING','PLANNING','WEAVING','MENDING','NEEDLING','FINISHING','SEAMING') ";
            System.out.println("SalePRFinishing:" + sql);
            stmt.execute(sql);

            stmt.close();
            Conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void UpdateWarping() {
        try {            
            sql = "UPDATE PRODUCTION.FELT_WARPING_BEAM_ORDER_DETAIL A, PRODUCTION.FELT_WARPING_BEAM_ORDER_HEADER B, "
                    + " PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_WARP_DATE = '0000-00-00', WIP_PIECE_STAGE = 'PLANNING', WIP_STATUS = 'CONFIRMED' "
                    + " WHERE PIECE_NO  = WIP_EXT_PIECE_NO   "
                    + "AND A.DOC_NO = B.DOC_NO AND COALESCE(B.APPROVED,0) =1 AND COALESCE(CANCELED,0) =0  AND INDICATOR = 'DELETE'  ";
            System.out.println("Warping-1:" + sql);
            //stmt.execute(sql);
                        
            
            sql = "UPDATE PRODUCTION.FELT_WARPING_BEAM_ORDER_DETAIL A, PRODUCTION.FELT_WARPING_BEAM_ORDER_HEADER B, "
                    + " PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_WARP_DATE = A.INDICATOR_DATE, WIP_PIECE_STAGE = 'WEAVING', WIP_STATUS = 'WARPED' "
                    + " WHERE PIECE_NO  = WIP_EXT_PIECE_NO AND WIP_PIECE_STAGE ='PLANNING' AND WIP_WARP_DATE ='0000-00-00'  "
                    + "AND A.DOC_NO = B.DOC_NO AND COALESCE(B.APPROVED,0) =1 AND COALESCE(CANCELED,0) =0  AND INDICATOR != 'DELETE'  ";
            System.out.println("Warping-1:" + sql);
            stmt.execute(sql);

            sql = "UPDATE PRODUCTION.FELT_WARPING_BEAM_ORDER_DETAIL A, PRODUCTION.FELT_WARPING_BEAM_ORDER_HEADER B, "
                    + " PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_WARP_A_DATE = A.INDICATOR_DATE WHERE PIECE_NO  = WIP_EXT_PIECE_NO AND WIP_PIECE_STAGE ='PLANNING' AND WIP_WARP_DATE ='0000-00-00'  "
                    + "AND A.DOC_NO = B.DOC_NO AND COALESCE(B.APPROVED,0) =1 AND COALESCE(CANCELED,0) =0  AND INDICATOR != 'DELETE'  AND (PIECE_NO LIKE '%A'   OR (PIECE_NO NOT LIKE '%A' AND PIECE_NO NOT LIKE '%B'))";
            System.out.println("Warping-2:" + sql);
            stmt.execute(sql);

            sql = "UPDATE PRODUCTION.FELT_WARPING_BEAM_ORDER_DETAIL A, PRODUCTION.FELT_WARPING_BEAM_ORDER_HEADER B, "
                    + " PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_WARP_B_DATE = A.INDICATOR_DATE WHERE PIECE_NO  = WIP_EXT_PIECE_NO AND WIP_PIECE_STAGE ='PLANNING' AND WIP_WARP_DATE ='0000-00-00'  "
                    + "AND A.DOC_NO = B.DOC_NO AND COALESCE(B.APPROVED,0) =1 AND COALESCE(CANCELED,0) =0  AND INDICATOR != 'DELETE'  AND PIECE_NO LIKE '%B' ";
            System.out.println("Warping-3:" + sql);
            stmt.execute(sql);

            sql = "UPDATE PRODUCTION.FELT_WARPING_BEAM_ORDER_DRYER_DETAIL A, PRODUCTION.FELT_WARPING_BEAM_ORDER_DRYER_HEADER B, "
                    + " PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_WARP_DATE = A.INDICATOR_DATE,WIP_WARP_A_DATE = A.INDICATOR_DATE, WIP_PIECE_STAGE = 'WEAVING', WIP_STATUS = 'WARPED' "
                    + " WHERE PIECE_NO  = WIP_EXT_PIECE_NO AND WIP_PIECE_STAGE ='PLANNING' AND WIP_WARP_DATE ='0000-00-00'  "
                    + "AND A.DOC_NO = B.DOC_NO AND COALESCE(B.APPROVED,0) =1 AND COALESCE(CANCELED,0) =0  AND INDICATOR != 'DELETE' ";
            System.out.println("Warping-4:" + sql);
            stmt.execute(sql);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void UpdateWeaving() {
        try {
            sql = "UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER W,"
                    + "("
                    + "SELECT TRIM(WIP_EXT_PIECE_NO) AS EXTPIECE,MAX(PROD_DATE) AS MAXDATE,GROUP_CONCAT(WIP_EXT_PIECE_NO,' (',DATE_FORMAT(PROD_DATE,'%d/%m/%Y'), ') ' ORDER BY WIP_EXT_PIECE_NO SEPARATOR ', ') AS PIECEPROD ,"
                    + "SUM(WEIGHT) AS SUMWEIGHT "
                    + "FROM PRODUCTION.FELT_WIP_PIECE_REGISTER,PRODUCTION.FELT_PROD_DATA "
                    + "WHERE WIP_EXT_PIECE_NO = PROD_PIECE_NO AND PROD_DEPT ='WEAVING' AND CANCELED =0 AND APPROVED =1 "
                    + " AND WIP_STYLE NOT IN ('Y 18271-5','Y 18272-5')  "
                    + " AND WIP_PIECE_STAGE IN ('BOOKING','PLANNING','WEAVING','MENDING','NEEDLING','FINISHING','SEAMING') "
                    + "GROUP BY TRIM(WIP_EXT_PIECE_NO) "
                    + ") AS A "
                    + "SET WIP_PIECE_STAGE='MENDING',WIP_STATUS='WOVEN',WIP_WVG_DATE = MAXDATE, WIP_WVG_LAYER_REMARK = PIECEPROD, WIP_WEAVING_WEIGHT = SUMWEIGHT,WIP_WVG_A_DATE = MAXDATE,  WIP_WEAVING_WEIGHT_A = SUMWEIGHT  "
                    + "WHERE A.EXTPIECE = W.WIP_EXT_PIECE_NO AND COALESCE(WIP_PIECE_AB_FLAG,'') !='AB' "
                    + " AND COALESCE(WIP_WVG_DATE,'0000-00-00')='0000-00-00' AND WIP_PRIORITY_HOLD_CAN_FLAG IN (0,1,2,3,4,5)";
            System.out.println("Weaving without AB:" + sql);
            stmt.execute(sql);

            sql = "UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER W,"
                    + "("
                    + "SELECT TRIM(WIP_EXT_PIECE_NO) AS EXTPIECE,MAX(PROD_DATE) AS MAXDATE,GROUP_CONCAT(WIP_EXT_PIECE_NO,' (',DATE_FORMAT(PROD_DATE,'%d/%m/%Y'), ') ' ORDER BY WIP_EXT_PIECE_NO SEPARATOR ', ') AS PIECEPROD ,"
                    + "MAX(CASE WHEN RIGHT(WIP_EXT_PIECE_NO,2) ='-A' THEN PROD_DATE ELSE '0000-00-00' END) AS A,"
                    + "MAX(CASE WHEN RIGHT(WIP_EXT_PIECE_NO,2) ='-B' THEN PROD_DATE ELSE '0000-00-00' END) AS B,"
                    + "SUM(CASE WHEN RIGHT(WIP_EXT_PIECE_NO,2) ='-A' THEN WEIGHT ELSE 0 END) AS AA,"
                    + "SUM(CASE WHEN RIGHT(WIP_EXT_PIECE_NO,2) ='-B' THEN WEIGHT ELSE 0 END) AS BB,"
                    + "SUM(WEIGHT) AS SUMWEIGHT "
                    + "FROM PRODUCTION.FELT_WIP_PIECE_REGISTER,PRODUCTION.FELT_PROD_DATA "
                    + "WHERE WIP_EXT_PIECE_NO = PROD_PIECE_NO AND PROD_DEPT ='WEAVING' AND CANCELED =0 AND APPROVED =1 "
                    + " AND WIP_STYLE NOT IN ('Y 18271-5','Y 18272-5') "
                    + " AND COALESCE(WIP_PIECE_AB_FLAG,'') ='AB' AND WIP_PIECE_STAGE IN ('BOOKING','PLANNING','WEAVING','MENDING','NEEDLING','FINISHING','SEAMING') "
                    + "GROUP BY TRIM(WIP_EXT_PIECE_NO) "
                    + ") AS A "
                    + "SET WIP_PIECE_STAGE='MENDING',WIP_STATUS='WOVEN',WIP_WVG_DATE = MAXDATE, WIP_WVG_LAYER_REMARK = PIECEPROD, WIP_WEAVING_WEIGHT = SUMWEIGHT,WIP_WVG_A_DATE = A, WIP_WVG_B_DATE =B, WIP_WEAVING_WEIGHT_A = AA , WIP_WEAVING_WEIGHT_B = BB "
                    + "WHERE A.EXTPIECE = W.WIP_EXT_PIECE_NO AND COALESCE(WIP_PIECE_AB_FLAG,'') ='AB' "
                    + "  AND COALESCE(WIP_WVG_DATE,'0000-00-00')='0000-00-00' AND WIP_PRIORITY_HOLD_CAN_FLAG IN (0,1,2,3,4,5)";
            System.out.println("Weaving with AB:" + sql);
            stmt.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void UpdateMending() {
        try {
            sql = "UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER W,"
                    + "("
                    + "SELECT TRIM(WIP_EXT_PIECE_NO) AS EXTPIECE,MAX(PROD_DATE) AS MAXDATE,GROUP_CONCAT(WIP_EXT_PIECE_NO,' (',DATE_FORMAT(PROD_DATE,'%d/%m/%Y'), ') ' ORDER BY WIP_EXT_PIECE_NO SEPARATOR ', ') AS PIECEPROD ,"
                    + "SUM(WEIGHT) AS SUMWEIGHT "
                    + "FROM PRODUCTION.FELT_WIP_PIECE_REGISTER,PRODUCTION.FELT_PROD_DATA "
                    + "WHERE WIP_EXT_PIECE_NO = PROD_PIECE_NO AND PROD_DEPT ='MENDING' AND CANCELED =0 AND APPROVED =1   AND WIP_PIECE_STAGE IN ('BOOKING','PLANNING','WEAVING','MENDING','NEEDLING','FINISHING','SEAMING') "
                    + "GROUP BY TRIM(WIP_EXT_PIECE_NO) "
                    + ") AS A "
                    + "SET WIP_PIECE_STAGE='NEEDLING',WIP_STATUS='MENDED',WIP_MND_DATE = MAXDATE, WIP_MND_LAYER_REMARK = PIECEPROD, WIP_MENDING_WEIGHT = SUMWEIGHT,WIP_MND_A_DATE = MAXDATE,  WIP_MENDING_WEIGHT_A = SUMWEIGHT  "
                    + "WHERE A.EXTPIECE = W.WIP_EXT_PIECE_NO AND COALESCE(WIP_PIECE_AB_FLAG,'') !='AB' "
                    + " AND WIP_GROUP NOT IN ('HDS','M50','M35') AND COALESCE(WIP_MND_DATE,'0000-00-00')='0000-00-00' AND WIP_PRIORITY_HOLD_CAN_FLAG IN (0,1,2,3,4,5)";
            System.out.println("Mending without AB:" + sql);
            stmt.execute(sql);

            sql = "UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER W,"
                    + "("
                    + "SELECT TRIM(WIP_EXT_PIECE_NO) AS EXTPIECE,MAX(PROD_DATE) AS MAXDATE,GROUP_CONCAT(WIP_EXT_PIECE_NO,' (',DATE_FORMAT(PROD_DATE,'%d/%m/%Y'), ') ' ORDER BY WIP_EXT_PIECE_NO SEPARATOR ', ') AS PIECEPROD ,"
                    + "MAX(CASE WHEN RIGHT(WIP_EXT_PIECE_NO,2) ='-A' THEN PROD_DATE ELSE '0000-00-00' END) AS A,"
                    + "MAX(CASE WHEN RIGHT(WIP_EXT_PIECE_NO,2) ='-B' THEN PROD_DATE ELSE '0000-00-00' END) AS B,"
                    + "SUM(CASE WHEN RIGHT(WIP_EXT_PIECE_NO,2) ='-A' THEN WEIGHT ELSE 0 END) AS AA,"
                    + "SUM(CASE WHEN RIGHT(WIP_EXT_PIECE_NO,2) ='-B' THEN WEIGHT ELSE 0 END) AS BB,"
                    + "SUM(WEIGHT) AS SUMWEIGHT "
                    + "FROM PRODUCTION.FELT_WIP_PIECE_REGISTER,PRODUCTION.FELT_PROD_DATA "
                    + "WHERE WIP_EXT_PIECE_NO = PROD_PIECE_NO AND PROD_DEPT ='MENDING' AND CANCELED =0 AND APPROVED =1  AND COALESCE(WIP_PIECE_AB_FLAG,'') ='AB' AND WIP_PIECE_STAGE IN ('BOOKING','PLANNING','WEAVING','MENDING','NEEDLING','FINISHING','SEAMING') "
                    + "GROUP BY TRIM(WIP_EXT_PIECE_NO) "
                    + ") AS A "
                    + "SET WIP_PIECE_STAGE='NEEDLING',WIP_STATUS='MENDED',WIP_MND_DATE = MAXDATE, WIP_MND_LAYER_REMARK = PIECEPROD, WIP_MENDING_WEIGHT = SUMWEIGHT,WIP_MND_A_DATE = A, WIP_MND_B_DATE =B, WIP_MENDING_WEIGHT_A = AA , WIP_MENDING_WEIGHT_B = BB "
                    + "WHERE A.EXTPIECE = W.WIP_EXT_PIECE_NO AND COALESCE(WIP_PIECE_AB_FLAG,'') ='AB' "
                    + "  AND WIP_GROUP NOT IN ('HDS','M50','M35') AND COALESCE(WIP_MND_DATE,'0000-00-00')='0000-00-00' AND WIP_PRIORITY_HOLD_CAN_FLAG IN (0,1,2,3,4,5)";
            System.out.println("Mending with AB:" + sql);
            stmt.execute(sql);

            sql = "UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER W,"
                    + "("
                    + "SELECT TRIM(WIP_EXT_PIECE_NO) AS EXTPIECE,MAX(PROD_DATE) AS MAXDATE,GROUP_CONCAT(WIP_EXT_PIECE_NO,' (',DATE_FORMAT(PROD_DATE,'%d/%m/%Y'), ') ' ORDER BY WIP_EXT_PIECE_NO SEPARATOR ', ') AS PIECEPROD ,"
                    + "SUM(WEIGHT) AS SUMWEIGHT "
                    + "FROM PRODUCTION.FELT_WIP_PIECE_REGISTER,PRODUCTION.FELT_PROD_DATA "
                    + "WHERE WIP_EXT_PIECE_NO = PROD_PIECE_NO AND PROD_DEPT ='MENDING' AND CANCELED =0 AND APPROVED =1   AND WIP_PIECE_STAGE IN ('BOOKING','PLANNING','WEAVING','MENDING','NEEDLING','FINISHING','SEAMING') "
                    + "GROUP BY TRIM(WIP_EXT_PIECE_NO) "
                    + ") AS A "
                    + "SET WIP_PIECE_STAGE='SEAMING',WIP_STATUS='MENDED',WIP_MND_DATE = MAXDATE, WIP_MND_LAYER_REMARK = PIECEPROD, WIP_MENDING_WEIGHT = SUMWEIGHT,WIP_MND_A_DATE = MAXDATE,  WIP_MENDING_WEIGHT_A = SUMWEIGHT  "
                    + "WHERE A.EXTPIECE = W.WIP_EXT_PIECE_NO AND COALESCE(WIP_PIECE_AB_FLAG,'') !='AB' "
                    + " AND WIP_GROUP IN ('HDS','M50','M35') AND COALESCE(WIP_MND_DATE,'0000-00-00')='0000-00-00' AND WIP_PRIORITY_HOLD_CAN_FLAG IN (0,1,2,3,4,5)";
            System.out.println("Mending [seaming] without AB:" + sql);
            stmt.execute(sql);

            sql = "UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER W,"
                    + "("
                    + "SELECT TRIM(WIP_EXT_PIECE_NO) AS EXTPIECE,MAX(PROD_DATE) AS MAXDATE,GROUP_CONCAT(WIP_EXT_PIECE_NO,' (',DATE_FORMAT(PROD_DATE,'%d/%m/%Y'), ') ' ORDER BY WIP_EXT_PIECE_NO SEPARATOR ', ') AS PIECEPROD ,"
                    + "MAX(CASE WHEN RIGHT(WIP_EXT_PIECE_NO,2) ='-A' THEN PROD_DATE ELSE '0000-00-00' END) AS A,"
                    + "MAX(CASE WHEN RIGHT(WIP_EXT_PIECE_NO,2) ='-B' THEN PROD_DATE ELSE '0000-00-00' END) AS B,"
                    + "SUM(CASE WHEN RIGHT(WIP_EXT_PIECE_NO,2) ='-A' THEN WEIGHT ELSE 0 END) AS AA,"
                    + "SUM(CASE WHEN RIGHT(WIP_EXT_PIECE_NO,2) ='-B' THEN WEIGHT ELSE 0 END) AS BB,"
                    + "SUM(WEIGHT) AS SUMWEIGHT "
                    + "FROM PRODUCTION.FELT_WIP_PIECE_REGISTER,PRODUCTION.FELT_PROD_DATA "
                    + "WHERE WIP_EXT_PIECE_NO = PROD_PIECE_NO AND PROD_DEPT ='MENDING' AND CANCELED =0 AND APPROVED =1  AND COALESCE(WIP_PIECE_AB_FLAG,'') ='AB' AND WIP_PIECE_STAGE IN ('BOOKING','PLANNING','WEAVING','MENDING','NEEDLING','FINISHING','SEAMING') "
                    + "GROUP BY TRIM(WIP_EXT_PIECE_NO) "
                    + ") AS A "
                    + "SET WIP_PIECE_STAGE='SEAMING',WIP_STATUS='MENDED',WIP_MND_DATE = MAXDATE, WIP_MND_LAYER_REMARK = PIECEPROD, WIP_MENDING_WEIGHT = SUMWEIGHT,WIP_MND_A_DATE = A, WIP_MND_B_DATE =B, WIP_MENDING_WEIGHT_A = AA , WIP_MENDING_WEIGHT_B = BB "
                    + "WHERE A.EXTPIECE = W.WIP_EXT_PIECE_NO AND COALESCE(WIP_PIECE_AB_FLAG,'') ='AB' "
                    + "  AND WIP_GROUP IN ('HDS','M50','M35') AND COALESCE(WIP_MND_DATE,'0000-00-00')='0000-00-00' AND WIP_PRIORITY_HOLD_CAN_FLAG IN (0,1,2,3,4,5)";
            System.out.println("Mending [seaming] with AB:" + sql);
            stmt.execute(sql);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void UpdateNeedling() {
        try {
            sql = "UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER W,"
                    + "("
                    + "SELECT TRIM(WIP_EXT_PIECE_NO) AS EXTPIECE,MAX(PROD_DATE) AS MAXDATE,GROUP_CONCAT(WIP_EXT_PIECE_NO,' (',DATE_FORMAT(PROD_DATE,'%d/%m/%Y'), ') ' ORDER BY WIP_EXT_PIECE_NO SEPARATOR ', ') AS PIECEPROD ,"
                    + "SUM(WEIGHT) AS SUMWEIGHT "
                    + "FROM PRODUCTION.FELT_WIP_PIECE_REGISTER,PRODUCTION.FELT_PROD_DATA "
                    + "WHERE WIP_EXT_PIECE_NO = PROD_PIECE_NO AND PROD_DEPT ='NEEDLING' AND CANCELED =0 AND APPROVED =1   AND WIP_PIECE_STAGE IN ('BOOKING','PLANNING','WEAVING','MENDING','NEEDLING','FINISHING','SEAMING') "
                    + "GROUP BY TRIM(WIP_EXT_PIECE_NO) "
                    + ") AS A "
                    + "SET WIP_PIECE_STAGE='FINISHING',WIP_STATUS='NEEDLED',WIP_NDL_DATE = MAXDATE,  WIP_NEEDLING_WEIGHT = SUMWEIGHT  "
                    + "WHERE A.EXTPIECE = W.WIP_EXT_PIECE_NO "
                    + "  AND COALESCE(WIP_NDL_DATE,'0000-00-00')='0000-00-00' AND WIP_PRIORITY_HOLD_CAN_FLAG IN (0,1,2,3,4,5)";
            System.out.println("NEEDLING:" + sql);
            stmt.execute(sql);

            stmt.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void UpdateHeatSetting() {
        try {
            sql = "UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER W,"
                    + "("
                    + "SELECT TRIM(WIP_EXT_PIECE_NO) AS EXTPIECE,MAX(PROD_DATE) AS MAXDATE,GROUP_CONCAT(WIP_EXT_PIECE_NO,' (',DATE_FORMAT(PROD_DATE,'%d/%m/%Y'), ') ' ORDER BY WIP_EXT_PIECE_NO SEPARATOR ', ') AS PIECEPROD ,"
                    + "SUM(WEIGHT) AS SUMWEIGHT "
                    + "FROM PRODUCTION.FELT_WIP_PIECE_REGISTER,PRODUCTION.FELT_PROD_DATA "
                    + "WHERE WIP_EXT_PIECE_NO = PROD_PIECE_NO AND PROD_DEPT ='HEAT_SETTING' AND CANCELED =0 AND APPROVED =1   AND WIP_PIECE_STAGE IN ('BOOKING','PLANNING','WEAVING','MENDING','NEEDLING','FINISHING','SEAMING','HEAT_SETTING') "
                    + "GROUP BY TRIM(WIP_EXT_PIECE_NO) "
                    + ") AS A "
                    + "SET WIP_PIECE_STAGE='NEEDLING',WIP_STATUS='HEAT_SET',WIP_HEATSET_DATE = MAXDATE,  WIP_HEATSETTING_WEIGHT = SUMWEIGHT  "
                    + "WHERE A.EXTPIECE = W.WIP_EXT_PIECE_NO "
                    + "  AND COALESCE(WIP_HEATSET_DATE,'0000-00-00')='0000-00-00' AND WIP_PRIORITY_HOLD_CAN_FLAG IN (0,1,2,3,4,5)";
            System.out.println("HEATSETTING:" + sql);
            stmt.execute(sql);

            stmt.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void UpdateSeaming() {
        try {
            sql = "UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER W,"
                    + "("
                    + "SELECT TRIM(WIP_EXT_PIECE_NO) AS EXTPIECE,MAX(PROD_DATE) AS MAXDATE,GROUP_CONCAT(WIP_EXT_PIECE_NO,' (',DATE_FORMAT(PROD_DATE,'%d/%m/%Y'), ') ' ORDER BY WIP_EXT_PIECE_NO SEPARATOR ', ') AS PIECEPROD ,"
                    + "SUM(WEIGHT) AS SUMWEIGHT "
                    + "FROM PRODUCTION.FELT_WIP_PIECE_REGISTER,PRODUCTION.FELT_PROD_DATA "
                    + "WHERE WIP_EXT_PIECE_NO = PROD_PIECE_NO AND PROD_DEPT ='SEAMING' AND CANCELED =0 AND APPROVED =1   AND WIP_PIECE_STAGE IN ('BOOKING','PLANNING','WEAVING','MENDING','NEEDLING','FINISHING','SEAMING') "
                    + "GROUP BY TRIM(WIP_EXT_PIECE_NO) "
                    + ") AS A "
                    + "SET WIP_PIECE_STAGE='FINISHING',WIP_STATUS='SEAMED',WIP_SEAM_DATE = MAXDATE, WIP_SEAM_WEIGHT = SUMWEIGHT  "
                    + "WHERE A.EXTPIECE = W.WIP_EXT_PIECE_NO  "
                    + " AND COALESCE(WIP_SEAM_DATE,'0000-00-00')='0000-00-00' AND WIP_PRIORITY_HOLD_CAN_FLAG IN (0,1,2,3,4,5)";
            System.out.println("Seaming:" + sql);
            stmt.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void UpdateFinishing() {
        try {
            sql = "UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER W,"
                    + "("
                    + "SELECT TRIM(WIP_EXT_PIECE_NO) AS EXTPIECE,MAX(PROD_DATE) AS MAXDATE,GROUP_CONCAT(WIP_EXT_PIECE_NO,' (',DATE_FORMAT(PROD_DATE,'%d/%m/%Y'), ') ' ORDER BY WIP_EXT_PIECE_NO SEPARATOR ', ') AS PIECEPROD ,"
                    + "SUM(WEIGHT) AS SUMWEIGHT "
                    + "FROM PRODUCTION.FELT_WIP_PIECE_REGISTER,PRODUCTION.FELT_PROD_DATA "
                    + "WHERE WIP_EXT_PIECE_NO = PROD_PIECE_NO AND PROD_DEPT ='FINISHING' AND CANCELED =0 AND APPROVED =1   AND WIP_PIECE_STAGE IN ('BOOKING','PLANNING','WEAVING','MENDING','NEEDLING','FINISHING','SEAMING') "
                    + "GROUP BY TRIM(WIP_EXT_PIECE_NO) "
                    + ") AS A "
                    + "SET WIP_PIECE_STAGE='IN STOCK',WIP_STATUS='FINISHED',WIP_FNSG_DATE = MAXDATE  "
                    + "WHERE A.EXTPIECE = W.WIP_EXT_PIECE_NO  "
                    + " AND COALESCE(WIP_FNSG_DATE,'0000-00-00')='0000-00-00' AND WIP_PRIORITY_HOLD_CAN_FLAG IN (0,1,2,3,4,5,6)";
            System.out.println("Finishing:" + sql);
            stmt.execute(sql);
            sql = "UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER W,"
                    + "("
                    + "SELECT TRIM(WIP_EXT_PIECE_NO) AS EXTPIECE,MAX(PROD_DATE) AS MAXDATE,GROUP_CONCAT(WIP_EXT_PIECE_NO,' (',DATE_FORMAT(PROD_DATE,'%d/%m/%Y'), ') ' ORDER BY WIP_EXT_PIECE_NO SEPARATOR ', ') AS PIECEPROD ,"
                    + "SUM(WEIGHT) AS SUMWEIGHT "
                    + "FROM PRODUCTION.FELT_WIP_PIECE_REGISTER,PRODUCTION.FELT_PROD_DATA "
                    + "WHERE WIP_EXT_PIECE_NO = PROD_PIECE_NO AND PROD_DEPT ='FELT FINISHING' AND CANCELED =0 AND APPROVED =1   AND WIP_PIECE_STAGE IN ('BOOKING','PLANNING','WEAVING','MENDING','NEEDLING','FINISHING','SEAMING') "
                    + "GROUP BY TRIM(WIP_EXT_PIECE_NO) "
                    + ") AS A "
                    + "SET WIP_PIECE_STAGE='IN STOCK',WIP_STATUS='FINISHED',WIP_FNSG_DATE = MAXDATE  "
                    + "WHERE A.EXTPIECE = W.WIP_EXT_PIECE_NO  "
                    + " AND COALESCE(WIP_FNSG_DATE,'0000-00-00')='0000-00-00' AND WIP_PRIORITY_HOLD_CAN_FLAG IN (0,1,2,3,4,5,6)";
            System.out.println("Finishing:" + sql);
            stmt.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
