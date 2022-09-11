/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP;

/**
 *
 * @author root
 */
public class clsPlaceOfSupply {
    
    public static String PlaceOfSupply(String stateCd) {

        String POS = "";
        try {

            if (stateCd.equalsIgnoreCase("01")) {
                POS = "01-Jammu & Kashmir";
            }

            if (stateCd.equalsIgnoreCase("02")) {
                POS = "02-Himachal Pradesh";
            }

            if (stateCd.equalsIgnoreCase("03")) {
                POS = "03-Punjab";
            }

            if (stateCd.equalsIgnoreCase("04")) {
                POS = "04-Chandigarh";
            }

            if (stateCd.equalsIgnoreCase("05")) {
                POS = "05-Uttarakhand";
            }

            if (stateCd.equalsIgnoreCase("06")) {
                POS = "06-Haryana";
            }

            if (stateCd.equalsIgnoreCase("07")) {
                POS = "07-Delhi";
            }

            if (stateCd.equalsIgnoreCase("08")) {
                POS = "08-Rajasthan";
            }

            if (stateCd.equalsIgnoreCase("09")) {
                POS = "09-Uttar Pradesh";
            }

            if (stateCd.equalsIgnoreCase("10")) {
                POS = "10-Bihar";
            }

            if (stateCd.equalsIgnoreCase("11")) {
                POS = "11-Sikkim";
            }

            if (stateCd.equalsIgnoreCase("12")) {
                POS = "12-Arunachal Pradesh";
            }

            if (stateCd.equalsIgnoreCase("13")) {
                POS = "13-Nagaland";
            }

            if (stateCd.equalsIgnoreCase("14")) {
                POS = "14-Manipur";
            }

            if (stateCd.equalsIgnoreCase("15")) {
                POS = "15-Mizoram";
            }

            if (stateCd.equalsIgnoreCase("16")) {
                POS = "16-Tripura";
            }

            if (stateCd.equalsIgnoreCase("17")) {
                POS = "17-Meghalaya";
            }

            if (stateCd.equalsIgnoreCase("18")) {
                POS = "18-Assam";
            }

            if (stateCd.equalsIgnoreCase("19")) {
                POS = "19-West Bengal";
            }

            if (stateCd.equalsIgnoreCase("20")) {
                POS = "20-Jharkhand";
            }

            if (stateCd.equalsIgnoreCase("21")) {
                POS = "21-Odisha";
            }

            if (stateCd.equalsIgnoreCase("22")) {
                POS = "22-Chhattisgarh";
            }

            if (stateCd.equalsIgnoreCase("23")) {
                POS = "23-Madhya Pradesh";
            }

            if (stateCd.equalsIgnoreCase("24")) {
                POS = "24-Gujarat";
            }

            if (stateCd.equalsIgnoreCase("25")) {
                POS = "25-Daman & Diu";
            }

            if (stateCd.equalsIgnoreCase("26")) {
                POS = "26-Dadra & Nagar Haveli";
            }

            if (stateCd.equalsIgnoreCase("27")) {
                POS = "27-Maharashtra";
            }

            if (stateCd.equalsIgnoreCase("28")) {
                POS = "";
            }

            if (stateCd.equalsIgnoreCase("29")) {
                POS = "29-Karnataka";
            }

            if (stateCd.equalsIgnoreCase("30")) {
                POS = "30-Goa";
            }

            if (stateCd.equalsIgnoreCase("31")) {
                POS = "31-Lakshdweep";
            }

            if (stateCd.equalsIgnoreCase("32")) {
                POS = "32-Kerala";
            }

            if (stateCd.equalsIgnoreCase("33")) {
                POS = "33-Tamil Nadu";
            }

            if (stateCd.equalsIgnoreCase("34")) {
                POS = "34-Pondicherry";
            }

            if (stateCd.equalsIgnoreCase("35")) {
                POS = "35-Andaman & Nicobar Islands";
            }

            if (stateCd.equalsIgnoreCase("36")) {
                POS = "36-Telengana";
            }

            if (stateCd.equalsIgnoreCase("37")) {
                POS = "37-Andhra Pradesh";
            }

            if (stateCd.equalsIgnoreCase("98")) {
                POS = "98-Other Territory";
            }

            return POS;

        } catch (Exception ex) {
            return "";
        }
    }
    
}
