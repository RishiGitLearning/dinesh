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
public class Order_No_Conversion {
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
    public static int Rate_DocNo_Only(String Doc_No)
    {
        Doc_No = Doc_No.substring(2);
        return Integer.parseInt(Doc_No);
    }
    public static String Rate_DocNo_STRING(int dno)
    {
        String Doc_No = "000000".substring((dno+"").length())+dno;
        Doc_No = "RM"+Doc_No;
        return Doc_No;
    }
    public static int Finishing_DocNo_Only(String Doc_No)
    {
        Doc_No = Doc_No.substring(2);
        return Integer.parseInt(Doc_No);
    }
    public static String Finishing_DocNo_STRING(int dno)
    {
        String Doc_No = "000000".substring((dno+"").length())+dno;
        Doc_No = "FF"+Doc_No;
        return Doc_No;
    }
    public static int PackingSLip_DocNo_Only(String Doc_No)
    {
        Doc_No = Doc_No.substring(2);
        return Integer.parseInt(Doc_No);
    }
    public static String PackingSLip_DocNo_STRING(int dno)
    {
        String Doc_No = "000000".substring((dno+"").length())+dno;
        Doc_No = "PS"+Doc_No;
        return Doc_No;
    }
}
