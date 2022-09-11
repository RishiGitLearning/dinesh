/*
 * TBand.java
 *
 * Created on June 26, 2007, 4:07 PM
 */

package TReportWriter;

/**
 *
 * @author  root
 */
import java.util.HashMap;
import java.util.Iterator;

public class TBand {
    
    public static final int ReportHeader=1;
    public static final int PageHeader=2;
    public static final int GroupHeader=3;
    public static final int Detail=4;
    public static final int GroupFooter=5;
    public static final int PageFooter=6;
    public static final int ReportFooter=7;
    public static final int Summary=8;
    

    //** Band Properties **//
    public int BandHeight=0;
    public boolean SplitBand=true;
    public int BandType=0;
    public int BandStartRow=0;
    public int SrNo=0;
    public String BandName="";
    public String GroupExpression="";
    public boolean StartGroupOnNewPage=false;
    
    //**Fields contained in the Band**//
    public HashMap colTextFields=new HashMap();
    public HashMap colDBFields=new HashMap();
    
    public TBand FooterBand;
   
    public boolean Selected=false;
    
    /** Creates a new instance of TBand */
    public TBand() {
        
    }
    
    public TBand getCopy()
    {
         TBand newBand=new TBand();
         
      try
      {

         
         newBand.colTextFields=new HashMap();
         newBand.colDBFields=new HashMap();
         
         newBand.BandHeight=this.BandHeight;
         newBand.SplitBand=this.SplitBand;
         newBand.BandType=this.BandType;
         newBand.BandStartRow=this.BandStartRow;
         newBand.SrNo=this.SrNo;
         newBand.BandName=this.BandName;
         newBand.GroupExpression=this.GroupExpression;
         newBand.Selected=this.Selected;
         newBand.StartGroupOnNewPage=this.StartGroupOnNewPage;
         
         Iterator elements=this.colTextFields.keySet().iterator();
         
         while(elements.hasNext())
         {
            Object key=elements.next();
            
            newBand.colTextFields.put(Integer.toString(newBand.colTextFields.size()+1), this.colTextFields.get(key));
         }
         

         elements=this.colDBFields.keySet().iterator();
         
         while(elements.hasNext())
         {
            Object key=elements.next();
            
            newBand.colDBFields.put(Integer.toString(newBand.colDBFields.size()+1), this.colDBFields.get(key));
         }
         
      }
      catch(Exception e)
      {
         
      }
         return newBand;
    }
    
}
