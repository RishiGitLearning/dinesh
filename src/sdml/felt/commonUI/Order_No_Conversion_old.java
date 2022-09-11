/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdml.felt.commonUI;

/**
 *
 * @author root
 */
public class Order_No_Conversion_old {
    public static int Order_No_Only(String Order_No)
    {
        Order_No = Order_No.substring(1);
        return Integer.parseInt(Order_No);
    }
    public static String Order_No_STRING(int no)
    {
        String Order_No = "0000000".substring((no+"").length())+no;
        Order_No = 'S'+Order_No;
        return Order_No;
    }
}
