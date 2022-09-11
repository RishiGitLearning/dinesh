/*
 * TElement.java
 *
 * Created on July 18, 2007, 10:44 AM
 */

package TReportWriter;

/**
 *
 * @author  root
 */
public class TElement {
    
    public int PositionX=0;
    public int PositionY=0;
    public String Content="";
    public boolean EvaluationPending=false;
    public int EvaluationTime=TReport.EvaluateNow;
    public String EvaluationGroup="";
    public String VariableName="";
    
    /** Creates a new instance of TElement */
    public TElement() {
    }
    
}
