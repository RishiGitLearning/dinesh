/*
 * TReportPage.java
 *
 * Created on June 26, 2007, 4:21 PM
 */

package TReportWriter;

/**
 *
 * @author  root
 */
import java.util.*;

public class TReportPage {

    public static final int Fanfold1512=1;
    public static final int Letter=2;
    public static final int A4=3;
    
    public static final int Portrait=1;
    public static final int Landscape=2;
    
    //** Report Page Properties **//
    public int PageHeight =100;
    public int PageWidth=80;
    public int PageSize=TReportPage.Letter;
    public int PaperOrientation=TReportPage.Portrait;
    
    //** Margins **//
    public int LeftMargin=0;
    public int RightMargin=0;
    public int TopMargin=0;
    public int BottomMargin=0;
        
    public HashMap PrintLines=new HashMap();
    
    /** Creates a new instance of TReportPage */
    public TReportPage() {
        
    }
    
}
