/*
 * TTextField.java
 *
 * Created on June 26, 2007, 3:35 PM
 */

package TReportWriter;

/**
 *
 * @author  root
 *
 */
public class TDBField {
    
    //*** Properties for Database Field ***//
        
    public static final int Left=1;
    public static final int Right=2;
    public static final int Center=3;
    
    //Content Properties
    public String Text="";
    public String DataType="String"; 
    public boolean MultiLine=false;
    public int Alignment=TDBField.Left;
    
    //Position
    public int Width=10;
    public int PositionX=0;
    public int PositionY=0;

    //Print Control Properties
    public String PrintCondition="";
    public String FormatString="";
    
    public String ReportValue="";
        
    public boolean selected=false;
    
    public boolean BlankWhenNull=false;
    
    public boolean ConvertToWords=false;
    
    /** Creates a new instance of TTextField */
    public TDBField() {
        
    }
    
public TDBField getCopy()
    {
      TDBField newDBField=new TDBField();
      
      newDBField.Text=this.Text;
      newDBField.DataType=this.DataType;
      newDBField.MultiLine=this.MultiLine;
      newDBField.Alignment=this.Alignment;
      newDBField.Width=this.Width;
      newDBField.PositionX=this.PositionX;
      newDBField.PositionY=this.PositionY;
      newDBField.PrintCondition=this.PrintCondition;
      newDBField.FormatString=this.FormatString;
      newDBField.ReportValue=this.ReportValue;
      newDBField.selected=this.selected;
      newDBField.BlankWhenNull=this.BlankWhenNull;
      newDBField.ConvertToWords=this.ConvertToWords;
      return newDBField;
      
    }
    
}
