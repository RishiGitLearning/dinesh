/*
 * TTextField.java
 *
 * Created on June 26, 2007, 3:35 PM
 */

package TReportWriter;

/**
 *
 * @author  root
 */
public class TTextField {
    
    //*** Properties for Text Field ***//
    public static final int Left=1;
    public static final int Right=2;
    public static final int Center=3;
    
    //Content Properties
    public String Text="";
    public boolean MultiLine=false;
    public int Alignment=TTextField.Left;
    
    //Position
    public int Width=10;
    public int PositionX=0;
    public int PositionY=0;

    //Print Control Properties
    public String PrintCondition="";
        
    //Designer Properties
    public boolean selected=false;
        
    
    /** Creates a new instance of TTextField */
    public TTextField() {
    }
    
    public TTextField getCopy()
    {
      TTextField newTextField=new TTextField();
      
      newTextField.Text=this.Text;
      newTextField.MultiLine=this.MultiLine;
      newTextField.Alignment=this.Alignment;
      newTextField.Width=this.Width;
      newTextField.PositionX=this.PositionX;
      newTextField.PositionY=this.PositionY;
      newTextField.PrintCondition=this.PrintCondition;
      newTextField.selected=this.selected;
      
      return newTextField;
      
    }
    
}
