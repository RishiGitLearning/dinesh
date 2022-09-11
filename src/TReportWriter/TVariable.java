/*
 * TVariable.java
 *
 * Created on November 20, 2007, 4:25 PM
 */

package TReportWriter;

/**
 *
 * @author  root
 */
public class TVariable {
    
    public String VariableName="";
    public String Expression="";
    public String Function="None";
    public int EvaluationTime=TReport.EvaluateNone;
    public String EvaluationGroup="";
    public boolean BuiltInVariable=false;
    public String VariableValue="";
    public int ResetOn=TReport.EvaluateNone;
    public String ResetGroupName="";
    
    /** Creates a new instance of TVariable */
    public TVariable() {
        
    }
    
}
