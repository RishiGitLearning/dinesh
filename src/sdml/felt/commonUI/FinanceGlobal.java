/*
 * FinanceGlobal.java
 *
 * Created on August 4, 2007, 11:55 AM
 */
package sdml.felt.commonUI;

/**
 *
 * @author
 */
public class FinanceGlobal {

    /**
     * Creates a new instance of FinanceGlobal
     */
    public FinanceGlobal() {
    }

    public static String FinURL = "jdbc:mysql://200.0.0.227:3306/";

    public static final int TYPE_PJV = 1;
    public static final int TYPE_PAYMENT = 2;
    public static final int TYPE_DEBIT_NOTE = 3;
    public static final int TYPE_CASH_VOUCHER = 4;
    public static final int TYPE_SALES_JOURNAL = 5;
    public static final int TYPE_RECEIPT = 6;
    public static final int TYPE_CREDIT_NOTE = 7;
    public static final int TYPE_CASH_RECEIPT_VOUCHER = 8;
    public static final int TYPE_JOURNAL = 9;
    public static final int TYPE_PAYMENT_2 = 10;
    public static final int TYPE_LC_JV = 11;
}
