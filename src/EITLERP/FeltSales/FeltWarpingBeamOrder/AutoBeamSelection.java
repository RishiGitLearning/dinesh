/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales.FeltWarpingBeamOrder;

import EITLERP.EITLERPGLOBAL;
import java.sql.ResultSet;
import java.util.HashMap;
import sdml.felt.commonUI.data;

/**
 *
 * @author Dharmendra
 */
public class AutoBeamSelection {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try {
            String sql;
             sql = "TRUNCATE TABLE PRODUCTION.TMP_BEAM_ALLOCATION";
            data.Execute(sql);

            sql = "INSERT INTO PRODUCTION.TMP_BEAM_ALLOCATION (BA_PIECE_NO,BA_PARTY_CODE,BA_MACHINE_NO,BA_POSITION_NO,BA_UPN,BA_LENGTH,BA_GROUP,BA_DESIGN_REVISION_NO) "
                    + "SELECT WIP_EXT_PIECE_NO,WIP_PARTY_CODE,WIP_MACHINE_NO,WIP_POSITION_NO,WIP_UPN,WIP_LENGTH,WIP_GROUP,WIP_DESIGN_REVISION_NO "
                    + "FROM PRODUCTION.FELT_WIP_PIECE_REGISTER "
                    + "WHERE  WIP_REQ_MTH_LAST_DDMMYY = CASE WHEN WIP_GROUP='SDF' THEN LAST_DAY(DATE_ADD(CURDATE(),INTERVAL 0 MONTH))  "
                    + "ELSE LAST_DAY(DATE_ADD(CURDATE(),INTERVAL 1 MONTH)) END  AND "
                    + "WIP_PIECE_STAGE IN ('PLANNING') AND WIP_PRIORITY_HOLD_CAN_FLAG=0 "
                    + "AND WIP_EXT_PIECE_NO NOT IN (SELECT PIECE_NO FROM PRODUCTION.FELT_WARPING_BEAM_ORDER_DETAIL_AMEND)  AND COALESCE(INDICATOR,'')!='DELETE' "
//                    + "WHERE  WIP_REQ_MTH_LAST_DDMMYY = CASE WHEN WIP_GROUP='SDF' THEN LAST_DAY(DATE_ADD(CURDATE(),INTERVAL -1 MONTH))  "
//                    + "ELSE LAST_DAY(DATE_ADD(CURDATE(),INTERVAL 0 MONTH)) END  AND "
//                    + "WIP_PIECE_STAGE IN ('PLANNING') AND WIP_PRIORITY_HOLD_CAN_FLAG=0 "
                    
                    + "ORDER BY WIP_DATE,WIP_EXT_PIECE_NO";
            data.Execute(sql);

//            sql = "UPDATE PRODUCTION.TMP_BEAM_ALLOCATION B,PRODUCTION.FELT_DESIGN_MASTER_HEADER H "
//                    + "SET BA_STYLE=STYLE,BA_REED=REED,BA_REED_SPACE=REED_SPACE "
//                    + "WHERE B.BA_PARTY_CODE=H.PARTY_CODE AND RIGHT(100+BA_MACHINE_NO,2)=RIGHT(100+MACHINE_NO,2) AND "
//                    + "RIGHT(100+BA_POSITION_NO,2)=RIGHT(100+POSITION_NO,2) AND COALESCE(H.APPROVED,0)=1 AND COALESCE(H.CANCELED,0)=0";
            sql = "UPDATE PRODUCTION.TMP_BEAM_ALLOCATION B,PRODUCTION.FELT_DESIGN_MASTER_HEADER H "
                    + "SET BA_STYLE=STYLE,BA_REED=REED,BA_REED_SPACE=REED_SPACE "
                    + "WHERE B.BA_DESIGN_REVISION_NO=H.DESIGN_REVISION_NO AND B.BA_PARTY_CODE=H.PARTY_CODE AND RIGHT(100+BA_MACHINE_NO,2)=RIGHT(100+MACHINE_NO,2) AND "
                    + "RIGHT(100+BA_POSITION_NO,2)=RIGHT(100+POSITION_NO,2) AND COALESCE(H.APPROVED,0)=1 AND COALESCE(H.CANCELED,0)=0";
            System.out.println("SQL 1 "+sql);
            data.Execute(sql);
            
            //Update B Part
//            sql = "UPDATE PRODUCTION.TMP_BEAM_ALLOCATION B,PRODUCTION.FELT_DESIGN_MASTER_HEADER H "
//                    + "SET BA_STYLE=STYLE_MULTILAYER,BA_REED=REED_MULTILAYER,BA_REED_SPACE=REED_SPACE_MULTILAYER "
//                    + "WHERE B.BA_PIECE_NO LIKE '%-B' AND B.BA_PARTY_CODE=H.PARTY_CODE AND RIGHT(100+BA_MACHINE_NO,2)=RIGHT(100+MACHINE_NO,2) AND "
//                    + "RIGHT(100+BA_POSITION_NO,2)=RIGHT(100+POSITION_NO,2) AND COALESCE(H.APPROVED,0)=1 AND COALESCE(H.CANCELED,0)=0";
             sql = "UPDATE PRODUCTION.TMP_BEAM_ALLOCATION B,PRODUCTION.FELT_DESIGN_MASTER_HEADER H "
                    + "SET BA_STYLE=STYLE_MULTILAYER,BA_REED=REED_MULTILAYER,BA_REED_SPACE=REED_SPACE_MULTILAYER "
                    + "WHERE B.BA_DESIGN_REVISION_NO=H.DESIGN_REVISION_NO AND B.BA_PIECE_NO LIKE '%-B' AND B.BA_PARTY_CODE=H.PARTY_CODE AND RIGHT(100+BA_MACHINE_NO,2)=RIGHT(100+MACHINE_NO,2) AND "
                    + "RIGHT(100+BA_POSITION_NO,2)=RIGHT(100+POSITION_NO,2) AND COALESCE(H.APPROVED,0)=1 AND COALESCE(H.CANCELED,0)=0";
             System.out.println("SQL 2 "+sql);
             data.Execute(sql);

            sql = "UPDATE PRODUCTION.TMP_BEAM_ALLOCATION B,PRODUCTION.PPC_LOOM_ALLOCATION_MASTER L "
                    + "SET BA_LOOM_NO=RIGHT(100+LOOM_NO,2) "
                    + "WHERE REED_SPACE_MIN<=BA_REED_SPACE AND REED_SPACE_MAX>=BA_REED_SPACE AND STYLE=BA_STYLE AND REED_CODE=BA_REED ";
            System.out.println("SQL 3 "+sql);
            data.Execute(sql);

            sql = "DROP TABLE PRODUCTION.TMPFELTWARPINGBEAMORDERHEADER";
            data.Execute(sql);
            sql = "CREATE TABLE PRODUCTION.TMPFELTWARPINGBEAMORDERHEADER "
                    + "SELECT DOC_NO, BEAM_NO, LOOM_NO, REED_SPACE, WARP_DETAIL, WARP_TEX, ENDS_10_CM, ACTUAL_WARP_RELISATION, WARP_LENGTH, "
                    + "REED_COUNT, FABRIC_REALISATION_PER, FROM_IP, HIERARCHY_ID, "
                    + "CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE, APPROVED, APPROVED_DATE, REJECTED, REJECTED_DATE, CANCELED, "
                    + "CHANGED, CHANGED_DATE, REJECTED_REMARKS, DOC_DATE, BEAM_CLOSURE_IND "
                    + "FROM PRODUCTION.FELT_WARPING_BEAM_ORDER_HEADER "
                    + "WHERE COALESCE(BEAM_CLOSURE_IND,0)=0 ";
            System.out.println("SQL WARPING HEADER "+sql);
            
            data.Execute(sql);
            sql = "DROP TABLE PRODUCTION.TMPFELTWARPINGBEAMORDERDETAIL";
            data.Execute(sql);
            sql = "CREATE TABLE PRODUCTION.TMPFELTWARPINGBEAMORDERDETAIL "
                    + "SELECT DOC_NO, BEAM_NO, LOOM_NO, SR_NO, PIECE_NO, PARTY_CODE, PRODUCT_CODE, GRUP, STYLE, LENGTH, WIDTH, GSM, "
                    + "WEIGHT, SEQUANCE_NO, READ_SPACE, THEORICAL_LENGTH_MTR, THEORICAL_PICKS_10_CM, TOTAL_PICKS, EXPECTED_GREV_SQ_MTR, "
                    + "FROM_IP, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE, CHANGED_DATE, CHANGED, "
                    + "INDICATOR, INDICATOR_DATE, INDICATOR_DOC, WEAVING_DATE, WEAVING_WEIGHT, WEAVING_SEQUENCE, PP_REMARK, "
                    + "WIP_OC_MONTHYEAR, WIP_OC_LAST_DDMMYY, AUTO_SEQUENCE "
                    + "FROM PRODUCTION.FELT_WARPING_BEAM_ORDER_DETAIL "
                    + "WHERE DOC_NO IN (SELECT DISTINCT DOC_NO FROM PRODUCTION.TMPFELTWARPINGBEAMORDERHEADER) ";
            data.Execute(sql);
            sql = "UPDATE PRODUCTION.TMP_BEAM_ALLOCATION A,PRODUCTION.FELT_DESIGN_MASTER_HEADER H "
                    + "SET BA_THEORICAL_PICKS_10_CM=(THEO_PICKS/10)/THEO_LENGTH,BA_LENGTH=THEO_LENGTH "
                    + "WHERE A.BA_DESIGN_REVISION_NO=H.DESIGN_REVISION_NO AND BA_UPN=UPN_NO "
                    + "AND COALESCE(H.APPROVED,0)=1 AND COALESCE(H.CANCELED,0)=0";
            data.Execute(sql);
            
            //Update B Part
            sql = "UPDATE PRODUCTION.TMP_BEAM_ALLOCATION A,PRODUCTION.FELT_DESIGN_MASTER_HEADER H "
                    + "SET BA_THEORICAL_PICKS_10_CM=PICKS_10CM_MULTILAYER "
                    + "WHERE A.BA_DESIGN_REVISION_NO=H.DESIGN_REVISION_NO AND BA_UPN=UPN_NO AND A.BA_PIECE_NO LIKE '%-B "
                    + "AND COALESCE(H.APPROVED,0)=1 AND COALESCE(H.CANCELED,0)=0";
            data.Execute(sql);
            
            ResultSet rs, wrs;
            sql = "SELECT * FROM PRODUCTION.TMP_BEAM_ALLOCATION  WHERE COALESCE(BA_LOOM_NO,0)!=0 AND COALESCE(BA_STYLE,'')!='' "
                    + "ORDER BY BA_PIECE_NO,BA_LOOM_NO";
            rs = data.getResult(sql);

            rs.first();
            if (rs.getRow() > 0) {
                HashMap mnewdoc = new HashMap();;
                String newdoc = "";
                int srno = 9999;
                while (!rs.isAfterLast()) {                    
                    
                    sql = "SELECT "
                            //                            + "WREEDSPACE,"
                            + "WARP_LENGTH-COALESCE(WMTR,0) AS RWMTR,WH.* FROM PRODUCTION.TMPFELTWARPINGBEAMORDERHEADER WH "
                            + "LEFT JOIN (SELECT SUM(THEORICAL_LENGTH_MTR) AS WMTR,DOC_NO "
                            + "FROM PRODUCTION.TMPFELTWARPINGBEAMORDERDETAIL "
                            + "WHERE LOOM_NO=" + rs.getString("BA_LOOM_NO") + "  GROUP BY DOC_NO) AS WD ON WH.DOC_NO=WD.DOC_NO "
                            //                            + "LEFT JOIN (SELECT READ_SPACE AS WREEDSPACE,DOC_NO FROM PRODUCTION.TMPFELTWARPINGBEAMORDERDETAIL "
                            //                            + "WHERE LOOM_NO=" + rs.getString("BA_LOOM_NO") + "  AND WEAVING_SEQUENCE=1 GROUP BY DOC_NO ) AS WRS ON WH.DOC_NO=WRS.DOC_NO "
                            + "WHERE COALESCE(BEAM_CLOSURE_IND,0)=0 AND LOOM_NO=" + rs.getString("BA_LOOM_NO") + " "
                            //                            + "AND WREEDSPACE>=" + rs.getString("BA_REED_SPACE") + " "
                            + "AND REED_COUNT='" + rs.getString("BA_REED") + "' "
                            + "AND COALESCE(WARP_LENGTH-COALESCE(WMTR,0),0)>=" + rs.getString("BA_LENGTH") + " "
                            + "ORDER BY DOC_NO";
                    wrs = data.getResult(sql);
                    wrs.first();
                    if (wrs.getRow() > 0) {
                        sql = "UPDATE PRODUCTION.TMP_BEAM_ALLOCATION SET BA_BEAM_TYPE='E',BA_BEAM_DOC_NO='" + wrs.getString("DOC_NO") + "' "
                                + "WHERE BA_PIECE_NO='" + rs.getString("BA_PIECE_NO") + "'";
                        data.Execute(sql);

                        sql = "INSERT INTO PRODUCTION.TMPFELTWARPINGBEAMORDERDETAIL "
                                + "(DOC_NO,BEAM_NO,LOOM_NO,SR_NO,PIECE_NO,READ_SPACE,THEORICAL_LENGTH_MTR,INDICATOR,THEORICAL_PICKS_10_CM,INDICATOR_DOC) "
                                + "VALUES ('" + wrs.getString("DOC_NO") + "','" + wrs.getString("BEAM_NO") + "','" + wrs.getString("LOOM_NO") + "','"
                                + srno + "','" + rs.getString("BA_PIECE_NO") + "',"
                                + "'" + rs.getString("BA_REED_SPACE") + "','" + rs.getString("BA_LENGTH") + "','INSERT',"
                                + rs.getString("BA_THEORICAL_PICKS_10_CM") + ",'SYSTEM')";
                        //System.out.println("Insert:" + sql);
                        data.Execute(sql);
                        srno--;
                    } else {
                        try {
                            newdoc = mnewdoc.get(rs.getString("BA_LOOM_NO")).toString();
                        } catch (Exception e) {
                            newdoc = "";
                        }
                        if (newdoc.equalsIgnoreCase("")) {
                            newdoc = "CONCAT('WBA',DATE_FORMAT(CURDATE(),'%Y%m%d'),'/1')";
                            mnewdoc.put(rs.getString("BA_LOOM_NO"), 1);
                        } else {
                            mnewdoc.put(rs.getString("BA_LOOM_NO"), (Integer.parseInt(newdoc) + 1));
                            newdoc = "CONCAT('WBA',DATE_FORMAT(CURDATE(),'%Y%m%d'),'/','" + (Integer.parseInt(newdoc) + 1) + "')";
                        }
                        sql = "INSERT INTO PRODUCTION.TMPFELTWARPINGBEAMORDERHEADER  "
                                + "SELECT " + newdoc + ",999 AS BEAM,BA_LOOM_NO,BA_REED_SPACE,WARP_YARN_DESC,D.TEX_COUNT,ENDS_10_CM,ACTUAL_WARP_LENGTH,"
                                + "ACHIEVABLE_WOVEN_LENGTH,REED_COUNT,0 AS FR_PER,NULL AS IP,2717 AS HIERARCHY,NULL,NULL,NULL,NULL,"
                                + "0,'0000-00-00',0,'0000-00-00',0,1,CURDATE(),'' AS REJ_RMK,NOW() AS DOC_DATE,0 AS BEAMCLOSIND "
                                + "FROM PRODUCTION.TMP_BEAM_ALLOCATION B,PRODUCTION.PPC_LOOM_ALLOCATION_MASTER M,PRODUCTION.FELT_WARPING_SELECTION_DATA D "
                                + "WHERE D.TYPE='MNE' AND BA_LOOM_NO=M.LOOM_NO AND "
                                + "REED_SPACE_MIN<=BA_REED_SPACE AND REED_SPACE_MAX>=BA_REED_SPACE AND STYLE=BA_STYLE AND REED_CODE=BA_REED AND "
                                + "M.TEX_COUNT=D.TEX_COUNT AND REED_COUNT=REED_CODE AND BA_PIECE_NO='" + rs.getString("BA_PIECE_NO") + "'";
                        data.Execute(sql);
                        int exe = data.getIntValueFromDB("SELECT COUNT(BA_LOOM_NO) FROM PRODUCTION.TMP_BEAM_ALLOCATION B,"
                                + "PRODUCTION.PPC_LOOM_ALLOCATION_MASTER M,PRODUCTION.FELT_WARPING_SELECTION_DATA D "
                                + "WHERE D.TYPE='MNE' AND BA_LOOM_NO=M.LOOM_NO AND "
                                + "REED_SPACE_MIN<=BA_REED_SPACE AND REED_SPACE_MAX>=BA_REED_SPACE AND STYLE=BA_STYLE AND REED_CODE=BA_REED AND "
                                + "M.TEX_COUNT=D.TEX_COUNT AND REED_COUNT=REED_CODE AND BA_PIECE_NO='" + rs.getString("BA_PIECE_NO") + "'");
                        if (exe > 0) {

                            rs.previous();
                        } else {
                            System.out.println("Check for piece " + rs.getString("BA_PIECE_NO"));
                        }
                    }
                    rs.next();
                }
                sql = "UPDATE PRODUCTION.TMPFELTWARPINGBEAMORDERDETAIL SET TOTAL_PICKS=ROUND((COALESCE(THEORICAL_LENGTH_MTR,0)*COALESCE(THEORICAL_PICKS_10_CM,0))*10,2)";
                data.Execute(sql);
                sql = "UPDATE PRODUCTION.TMPFELTWARPINGBEAMORDERDETAIL D,PRODUCTION.FELT_WIP_PIECE_REGISTER P "
                        + "SET D.WIP_OC_LAST_DDMMYY=WIP_REQ_MTH_LAST_DDMMYY WHERE D.PIECE_NO=P.WIP_EXT_PIECE_NO AND COALESCE(D.WIP_OC_LAST_DDMMYY,'')=''";
                data.Execute(sql);

                sql = "UPDATE PRODUCTION.TMPFELTWARPINGBEAMORDERDETAIL D,"
                        + "(SELECT   SEQUANCE_NO,PIECE_NO,CASE BEAM_NO  WHEN @curType THEN @curRow := @curRow + 1  ELSE @curRow := 1  END  AS rank,"
                        + "@curType := BEAM_NO AS BEAM_NO "
                        + "FROM PRODUCTION.TMPFELTWARPINGBEAMORDERDETAIL p "
                        + "JOIN (SELECT @curRow := 0,  @curType := '') r "
                        //+ "WHERE  INDICATOR != 'DELETE' "
                        //+ "AND COALESCE(WEAVING_DATE,'0000-00-00') ='0000-00-00' "
                        + "ORDER BY BEAM_NO,WIP_OC_LAST_DDMMYY,READ_SPACE DESC,THEORICAL_LENGTH_MTR DESC) AS A "
                        + "SET D.SEQUANCE_NO=rank "
                        + "WHERE A.BEAM_NO=D.BEAM_NO AND A.PIECE_NO=D.PIECE_NO ";
                data.Execute(sql);

                sql = "UPDATE PRODUCTION.TMPFELTWARPINGBEAMORDERDETAIL D,"
                        + "(SELECT   SEQUANCE_NO,PIECE_NO,CASE BEAM_NO  WHEN @curType THEN @curRow := @curRow + 1  ELSE @curRow := 1  END  AS rank,"
                        + "@curType := BEAM_NO AS BEAM_NO "
                        + "FROM PRODUCTION.TMPFELTWARPINGBEAMORDERDETAIL p "
                        + "JOIN (SELECT @curRow := 0,  @curType := '') r " 
                        + "WHERE  INDICATOR != 'DELETE' AND COALESCE(WEAVING_DATE,'0000-00-00') ='0000-00-00' "
                        + "ORDER BY BEAM_NO,SEQUANCE_NO) AS A "
                        + "SET WEAVING_SEQUENCE=rank "
                        + "WHERE A.BEAM_NO=D.BEAM_NO AND A.PIECE_NO=D.PIECE_NO";
                data.Execute(sql);

                HashMap total_pick = new HashMap();
                sql = "SELECT * FROM PRODUCTION.FELT_PICK_PER_DAY_MASTER";
                rs = data.getResult(sql);
                rs.first();
                if (rs.getRow() > 0) {
                    while (!rs.isAfterLast()) {
                        total_pick.put(rs.getInt("LOOM_NO"), rs.getInt("TOTAL_PICK"));
                        rs.next();
                    }

                }
                sql = "SELECT COUNT(*) FROM PRODUCTION.TMPFELTWARPINGBEAMORDERDETAIL  WHERE COALESCE(WEAVING_SEQUENCE,0)!=0 AND INDICATOR!='DELETE' "
                        + "ORDER BY DOC_NO,(WEAVING_SEQUENCE*1)";
                int totrc = data.getIntValueFromDB(sql);

                sql = "SELECT * FROM PRODUCTION.TMPFELTWARPINGBEAMORDERDETAIL  WHERE COALESCE(WEAVING_SEQUENCE,0)!=0 AND INDICATOR!='DELETE' "
                        + "ORDER BY DOC_NO,(WEAVING_SEQUENCE*1)";
                rs = data.getResult(sql);

                rs.first();
                String mdoc, lstwodt, nxtdt = "";

                int rpick = 0, pickpd, chkhl, update = 0, counter = 1;

                if (rs.getRow() > 0) {
                    mdoc = rs.getString("DOC_NO");
                    lstwodt = data.getStringValueFromDB("SELECT CASE WHEN COALESCE(MAX(WEAVING_DATE),'0000-00-00')='0000-00-00' THEN "
                            + "DATE_SUB(CURDATE(),INTERVAL 1 DAY) ELSE MAX(WEAVING_DATE) END "
                            + "FROM PRODUCTION.TMPFELTWARPINGBEAMORDERDETAIL WHERE DOC_NO='" + mdoc + "'");
                    if (mdoc.contains("/")) {
                        lstwodt = data.getStringValueFromDB("SELECT CASE WHEN COALESCE(MAX(BA_WEAVE_OUT_EXP_DATE),'0000-00-00')='0000-00-00' THEN "
                                + "DATE_ADD(CURDATE(),INTERVAL (WARP_PREPARATION_DAYS+BEAM_GAITING_DAYS) DAY) "
                                + "WHEN MAX(BA_WEAVE_OUT_EXP_DATE)<CURDATE() THEN DATE_ADD(CURDATE(),INTERVAL (WARP_PREPARATION_DAYS+BEAM_GAITING_DAYS) DAY) "
                                + "ELSE "
                                + "DATE_ADD(MAX(BA_WEAVE_OUT_EXP_DATE),INTERVAL (WARP_PREPARATION_DAYS+BEAM_GAITING_DAYS) DAY) "
                                + " END "
                                + " FROM PRODUCTION.TMP_BEAM_ALLOCATION "
                                + "LEFT JOIN PRODUCTION.LOOMWISE_PICK_WARP_MASTER ON LOOM_NO=BA_LOOM_NO "
                                + "WHERE BA_LOOM_NO=" + rs.getString("LOOM_NO") + " AND INSTR(BA_BEAM_DOC_NO,'/')=0");
                    }
                    pickpd = Integer.parseInt(total_pick.get(rs.getInt("LOOM_NO")).toString());

                    while (!rs.isAfterLast()) {
                        while (update == 0) {
                            if (!rs.getString("DOC_NO").equalsIgnoreCase(mdoc)) {
                                mdoc = rs.getString("DOC_NO");
                                lstwodt = data.getStringValueFromDB("SELECT CASE WHEN COALESCE(MAX(WEAVING_DATE),'0000-00-00')='0000-00-00' THEN DATE_SUB(CURDATE(),INTERVAL 1 DAY) ELSE MAX(WEAVING_DATE) END FROM PRODUCTION.TMPFELTWARPINGBEAMORDERDETAIL "
                                        + "WHERE DOC_NO='" + mdoc + "'");
                                if (mdoc.contains("/")) {
                                    lstwodt = data.getStringValueFromDB("SELECT CASE WHEN COALESCE(MAX(BA_WEAVE_OUT_EXP_DATE),'0000-00-00')='0000-00-00' THEN "
                                            + "DATE_ADD(CURDATE(),INTERVAL (WARP_PREPARATION_DAYS+BEAM_GAITING_DAYS) DAY) "
                                            + "WHEN MAX(BA_WEAVE_OUT_EXP_DATE)<CURDATE() THEN DATE_ADD(CURDATE(),INTERVAL (WARP_PREPARATION_DAYS+BEAM_GAITING_DAYS) DAY) "
                                            + "ELSE "
                                            + "DATE_ADD(MAX(BA_WEAVE_OUT_EXP_DATE),INTERVAL (WARP_PREPARATION_DAYS+BEAM_GAITING_DAYS) DAY) "
                                            + " END "
                                            + " FROM PRODUCTION.TMP_BEAM_ALLOCATION "
                                            + "LEFT JOIN PRODUCTION.LOOMWISE_PICK_WARP_MASTER ON LOOM_NO=BA_LOOM_NO "
                                            + "WHERE BA_LOOM_NO=" + rs.getString("LOOM_NO") + " AND INSTR(BA_BEAM_DOC_NO,'/')=0");
                                }
                                pickpd = Integer.parseInt(total_pick.get(rs.getInt("LOOM_NO")).toString());
                            }
                            if (pickpd >= rs.getInt("TOTAL_PICKS")) {
                                chkhl = 1;
                                while (chkhl == 1) {
                                    nxtdt = data.getStringValueFromDB("SELECT DATE_ADD('" + lstwodt + "',INTERVAL 1 DAY) FROM DUAL");
                                    chkhl = data.getIntValueFromDB("SELECT COUNT(*) FROM SDMLATTPAY.ATT_HOLIDAY_WEEKOFF "
                                            + "WHERE HL_DATE='" + nxtdt + "'");
                                    if (chkhl == 0) {
                                        data.Execute("UPDATE PRODUCTION.TMPFELTWARPINGBEAMORDERDETAIL "
                                                + "SET WEAVING_DATE='" + nxtdt + "'"
                                                + "WHERE PIECE_NO='" + rs.getString("PIECE_NO") + "'");
                                        update = 1;
                                    } else {
                                        lstwodt = nxtdt;
                                    }
                                }
                                pickpd = pickpd - rs.getInt("TOTAL_PICKS");
                            } else {
                                lstwodt = data.getStringValueFromDB("SELECT DATE_ADD('" + lstwodt + "',INTERVAL 1 DAY) FROM DUAL");
                                pickpd = pickpd + Integer.parseInt(total_pick.get(rs.getInt("LOOM_NO")).toString());
                            }
                        }
                        update = 0;
                        rs.next();
                        counter++;
                        System.out.println("Complete " + (counter * 100) / totrc + "%");
                        //System.out.println("Piece "+rs.getString("PIECE_NO"));
                    }
                }
                sql = "UPDATE PRODUCTION.TMP_BEAM_ALLOCATION B,PRODUCTION.TMPFELTWARPINGBEAMORDERDETAIL W "
                        + "SET BA_WEAVE_OUT_EXP_DATE=W.WEAVING_DATE,BA_WH_EXP_DATE=DATE_ADD(W.WEAVING_DATE,INTERVAL 1 MONTH) "
                        + "WHERE BA_PIECE_NO=PIECE_NO";
                data.Execute(sql);
                sql = "UPDATE PRODUCTION.TMP_BEAM_ALLOCATION SET BA_BEAM_TYPE='N' WHERE INSTR(BA_BEAM_DOC_NO,'/')>0";
                data.Execute(sql);

                sql = "INSERT INTO PRODUCTION.BEAM_ALLOCATION "
                        + "(BA_PIECE_NO, BA_LOOM_NO, BA_BEAM_TYPE, BA_WEAVE_OUT_EXP_DATE, BA_WH_EXP_DATE, BA_PROCESS, "
                        + "BA_PARTY_CODE, BA_MACHINE_NO, BA_POSITION_NO, BA_UPN, BA_STYLE, BA_REED, BA_REED_SPACE, BA_LENGTH, BA_BEAM_DOC_NO, BA_GROUP, "
                        + "BA_THEORICAL_PICKS_10_CM, "
                        + "REMARKS, HIERARCHY_ID, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE, APPROVED, APPROVED_DATE, REJECTED, REJECTED_DATE,"
                        + " REJECTED_REMARKS, CANCELED, CANCELED_DATE, CHANGED, CHANGED_DATE, BA_DOC_NO) "
                        + "SELECT BA_PIECE_NO, BA_LOOM_NO, BA_BEAM_TYPE, BA_WEAVE_OUT_EXP_DATE, BA_WH_EXP_DATE, BA_PROCESS, "
                        + "BA_PARTY_CODE, BA_MACHINE_NO, BA_POSITION_NO, BA_UPN, BA_STYLE, BA_REED, BA_REED_SPACE, BA_LENGTH, BA_BEAM_DOC_NO, BA_GROUP, "
                        + "BA_THEORICAL_PICKS_10_CM, "
                        + "'', 4855, '', NOW(), '', '', 0, '0000-00-00', 0, '0000-00-00', "
                        + "'', 0, '0000-00-00', 1, CURDATE(), CONCAT('BA',DATE_FORMAT(curdate(),'%Y%m%d')) FROM PRODUCTION.TMP_BEAM_ALLOCATION";

                data.Execute(sql);

                sql = "INSERT INTO PRODUCTION.FELT_PROD_DOC_DATA "
                        + "(MODULE_ID,DOC_NO,DOC_DATE,USER_ID,STATUS,TYPE,SR_NO) "
                        + "SELECT DISTINCT 875 AS MODULE_ID,BA_DOC_NO,CURDATE(),USER_ID,"
                        + "'W' AS STATUS,"
                        + "CASE WHEN APPROVAL_SEQUENCE=1 THEN 'C' ELSE 'A' END AS TYPE,"
                        + "APPROVAL_SEQUENCE AS SR_NO "
                        + " FROM PRODUCTION.BEAM_ALLOCATION I "
                        + "LEFT JOIN DINESHMILLS.D_COM_HIERARCHY_RIGHTS R ON R.HIERARCHY_ID=I.HIERARCHY_ID "
                        + "WHERE COALESCE(USER_ID,'')!='' AND BA_DOC_NO=CONCAT('BA',DATE_FORMAT(curdate(),'%Y%m%d'))";
                //data.Execute(sql);

                
            //INSERT IN WARPING BEAM AMEND 
            String qry_getBeamDocNo = "select distinct BA_BEAM_DOC_NO from  PRODUCTION.TMP_BEAM_ALLOCATION where BA_BEAM_DOC_NO LIKE 'WB__________' AND BA_BEAM_DOC_NO NOT LIKE '%\\%'";
            System.out.println("qry_getBeamDocNo :"+qry_getBeamDocNo);
            ResultSet rsData = data.getResult(qry_getBeamDocNo);
            rsData.first();
            while(!rsData.isAfterLast())
            {
                String BeamNo = data.getStringValueFromDB("SELECT BEAM_NO FROM PRODUCTION.FELT_WARPING_BEAM_ORDER_HEADER where DOC_NO='"+rsData.getString("BA_BEAM_DOC_NO")+"'");
                
                int MaxNo = EITLERP.data.getIntValueFromDB("SELECT COALESCE(MAX(SUBSTR(doc_no,locate('/',doc_no)+1)*1),0) FROM PRODUCTION.FELT_WARPING_BEAM_ORDER_HEADER_AMEND WHERE BEAM_NO='" + BeamNo + "'") + 1;
                String Doc_No = BeamNo + "/" + MaxNo;
                ResultSet rsTmp = EITLERP.data.getResult("SELECT USER()");
                rsTmp.first();
                String str = rsTmp.getString(1);
                String str_split[] = str.split("@");
            
                data.Execute("INSERT INTO PRODUCTION.FELT_WARPING_BEAM_ORDER_HEADER_AMEND  " +
                            "(DOC_NO, BEAM_NO, LOOM_NO, REED_SPACE, WARP_DETAIL, WARP_TEX, ENDS_10_CM, ACTUAL_WARP_RELISATION, "
                        + "WARP_LENGTH, REED_COUNT, FABRIC_REALISATION_PER, REASON, FROM_IP, HIERARCHY_ID, CREATED_BY, "
                        + "CREATED_DATE, MODIFIED_BY, MODIFIED_DATE, APPROVED, APPROVED_DATE, REJECTED, REJECTED_DATE, CANCELED, "
                        + "CHANGED, CHANGED_DATE, REJECTED_REMARKS) " +
                         "SELECT '"+Doc_No+"', '"+BeamNo+"', LOOM_NO, REED_SPACE, WARP_DETAIL, WARP_TEX, ENDS_10_CM, ACTUAL_WARP_RELISATION, "
                        + "WARP_LENGTH, REED_COUNT, FABRIC_REALISATION_PER,"
                        + " '' AS REASON, '"+str_split[1]+"', 2718, 59, '"+EITLERPGLOBAL.getCurrentDateDB()+"', 0, '0000-00-00', 0, "
                        + "'0000-00-00', 0, '0000-00-00', 0, 0, '0000-00-00', ''"
                        + " FROM PRODUCTION.FELT_WARPING_BEAM_ORDER_HEADER where DOC_NO='"+rsData.getString("BA_BEAM_DOC_NO")+"'");
                //INSERT IN PROD_DOC_DATA
                data.Execute("INSERT INTO PRODUCTION.FELT_PROD_DOC_DATA " +
                     "(MODULE_ID, DOC_NO, DOC_DATE, USER_ID, STATUS, TYPE, REMARKS, SR_NO, FROM_USER_ID, FROM_REMARKS, RECEIVED_DATE, ACTION_DATE, CHANGED, CHANGED_DATE) " +
                     " VALUES ('778', '"+Doc_No+"', '"+EITLERPGLOBAL.getCurrentDateDB()+"', '59', 'W', 'A', '', 1, '59', '', '"+EITLERPGLOBAL.getCurrentDateDB()+"', '0000-00-00', 0, '0000-00-00')");
                data.Execute("INSERT INTO PRODUCTION.FELT_PROD_DOC_DATA " +
                     "(MODULE_ID, DOC_NO, DOC_DATE, USER_ID, STATUS, TYPE, REMARKS, SR_NO, FROM_USER_ID, FROM_REMARKS, RECEIVED_DATE, ACTION_DATE, CHANGED, CHANGED_DATE) " +
                     " VALUES ('778', '"+Doc_No+"', '"+EITLERPGLOBAL.getCurrentDateDB()+"', '80', 'P', 'A', '', 2, '80', '', '0000-00-00', '0000-00-00', 0, '0000-00-00')");
                data.Execute("INSERT INTO PRODUCTION.FELT_PROD_DOC_DATA " +
                     "(MODULE_ID, DOC_NO, DOC_DATE, USER_ID, STATUS, TYPE, REMARKS, SR_NO, FROM_USER_ID, FROM_REMARKS, RECEIVED_DATE, ACTION_DATE, CHANGED, CHANGED_DATE) " +
                     " VALUES ('778', '"+Doc_No+"', '"+EITLERPGLOBAL.getCurrentDateDB()+"', '285', 'P', 'A', '', 3, '285', '', '0000-00-00', '0000-00-00', 0, '0000-00-00')");

                data.Execute("UPDATE PRODUCTION.TMPFELTWARPINGBEAMORDERDETAIL D,PRODUCTION.FELT_WIP_PIECE_REGISTER WP " +
                    "SET D.PARTY_CODE=WIP_PARTY_CODE , D.PRODUCT_CODE=WP.WIP_PRODUCT_CODE , D.GRUP=WP.WIP_GROUP " +
                    " , D.STYLE=WP.WIP_STYLE , D.LENGTH=WP.WIP_LENGTH , D.WIDTH=WP.WIP_WIDTH , D.GSM=WP.WIP_GSM  " +
                    " , D.WEIGHT=WP.WIP_BILL_WEIGHT, D.WIP_OC_MONTHYEAR=WP.WIP_OC_MONTHYEAR " +
                    " where D.PIECE_NO=WP.WIP_EXT_PIECE_NO AND BEAM_NO='"+BeamNo+"'");
                
                data.Execute("INSERT INTO PRODUCTION.FELT_WARPING_BEAM_ORDER_DETAIL_AMEND " +
                     " (DOC_NO, BEAM_NO, LOOM_NO, SR_NO, PIECE_NO, PARTY_CODE, PRODUCT_CODE, GRUP, STYLE, LENGTH, WIDTH, GSM, WEIGHT, SEQUANCE_NO, READ_SPACE, THEORICAL_LENGTH_MTR, THEORICAL_PICKS_10_CM, TOTAL_PICKS, EXPECTED_GREV_SQ_MTR, FROM_IP, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE, CHANGED_DATE, CHANGED, WEAVING_DATE, WEAVING_WEIGHT, WEAVING_SEQUENCE, PP_REMARK, WIP_OC_MONTHYEAR, WIP_OC_LAST_DDMMYY, AUTO_SEQUENCE) " +
                     " SELECT  " +
                     " '"+Doc_No+"', '"+BeamNo+"', LOOM_NO, CASE WHEN SR_NO>9000 THEN SR_NO ELSE @a := @a+1 END, PIECE_NO, PARTY_CODE, PRODUCT_CODE, GRUP, STYLE, LENGTH, WIDTH, GSM, WEIGHT, SEQUANCE_NO, READ_SPACE, THEORICAL_LENGTH_MTR, THEORICAL_PICKS_10_CM, TOTAL_PICKS, EXPECTED_GREV_SQ_MTR, FROM_IP, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE, CHANGED_DATE, CHANGED, WEAVING_DATE, WEAVING_WEIGHT, WEAVING_SEQUENCE, PP_REMARK, WIP_OC_MONTHYEAR, WIP_OC_LAST_DDMMYY, AUTO_SEQUENCE " +
                     " FROM " +
                     " PRODUCTION.TMPFELTWARPINGBEAMORDERDETAIL,(SELECT @a:= 0) AS a  where BEAM_NO='"+BeamNo+"'");
                     
                
                rsData.next();
            }
                
                 
//
//                data.Execute("INSERT INTO PRODUCTION.FELT_WARPING_BEAM_ORDER_DETAIL_AMEND " +
//                            "(DOC_NO, BEAM_NO, LOOM_NO, SR_NO, PIECE_NO, PARTY_CODE, PRODUCT_CODE, GRUP, STYLE, LENGTH, WIDTH, GSM, WEIGHT, SEQUANCE_NO, READ_SPACE, THEORICAL_LENGTH_MTR, THEORICAL_PICKS_10_CM, TOTAL_PICKS, EXPECTED_GREV_SQ_MTR, FROM_IP, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE, CHANGED_DATE, CHANGED, WEAVING_DATE, WEAVING_WEIGHT, WEAVING_SEQUENCE, PP_REMARK, WIP_OC_MONTHYEAR, WIP_OC_LAST_DDMMYY, AUTO_SEQUENCE) " +
//                            "(SELECT  " +
//                            "DOC_NO, BEAM_NO, LOOM_NO, SR_NO, PIECE_NO, PARTY_CODE, PRODUCT_CODE, GRUP, STYLE, LENGTH, WIDTH, GSM, WEIGHT, SEQUANCE_NO, READ_SPACE, THEORICAL_LENGTH_MTR, THEORICAL_PICKS_10_CM, TOTAL_PICKS, EXPECTED_GREV_SQ_MTR, FROM_IP, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE, CHANGED_DATE, CHANGED, WEAVING_DATE, WEAVING_WEIGHT, WEAVING_SEQUENCE, PP_REMARK, WIP_OC_MONTHYEAR, WIP_OC_LAST_DDMMYY, AUTO_SEQUENCE " +
//                            "FROM PRODUCTION.  " +
//                            ")");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
