/*
 * clsSalesParty.java
 *
 * Created on October 13, 2008, 1:26 PM
 */

package EITLERP.Sales;

/**
 *
 * @author  root
 */

import java.util.*;
import java.lang.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import EITLERP.Finance.*;

public class clsSalesParty {
    
    /** Creates a new instance of clsSalesParty */
    public clsSalesParty() {
    }
    
    
    public static String getAgentAlpha(String PartyCode) {
        try {
            String AgentCode=PartyCode.substring(0,2)+"0000";
            
            return data.getStringValueFromDB("SELECT TRIM(AREA_ID) AS AREA_ID FROM D_SAL_PARTY_MASTER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND PARTY_CODE='"+AgentCode+"' AND AREA_ID<>'' ");
        }
        catch(Exception e) {
            return "";
        }
    }
    
    
    
    
}
