/*
 * IIF.java
 *
 * Created on July 5, 2004, 10:27 AM
 */

package EITLERP;

/**
 *
 * @author  nrpithva
 */
 
import java.util.*;
import org.nfunk.jep.*;
import org.nfunk.jep.function.*;
import javax.swing.*;
import java.awt.*;
 
public class IIF extends PostfixMathCommand {
    
    private JEP Parser=new JEP();
    
    /** Creates a new instance of IIF */
    
    public IIF(JEP pParser) {
        Parser.initSymTab(); // clear the contents of the symbol table
        Parser.addStandardFunctions();
        Parser.addStandardConstants();
        
        SymbolTable srcSymbols=pParser.getSymbolTable();
        Enumeration theKeys=srcSymbols.keys();
        while(theKeys.hasMoreElements()) {
            Object theKey=theKeys.nextElement();
            Object theVal=srcSymbols.get(theKey.toString());
            if(!theKey.toString().trim().equals("i"))
            {
            Parser.addVariable(theKey.toString(),Double.parseDouble(theVal.toString()));
            }
        }
        
        Parser.addComplex(); // among other things adds i to the symbol table        
        numberOfParameters = 3;
    }
    
    public void run(Stack inStack) throws ParseException {
        try {
            int finalResult=0;
            
            // check the stack
            checkStack(inStack);
            
            // get the parameter from the stack
            Object param1 = inStack.get(0);
            Object param2 = inStack.get(1);
            Object param3 = inStack.get(2);
            
            inStack.clear();
            
            String compare=(String)param1;
            compare=compare.trim();
            double val1=Double.parseDouble(param2.toString());
            double val2=Double.parseDouble(param3.toString());

            Parser.parseExpression(compare);// Parse the expression
            Object result=Parser.getValueAsObject();
            
            if(result!=null) {
                finalResult=(int)Double.parseDouble(result.toString());
            }
            
            if(finalResult==1) {
                inStack.push(new Double(val1));
            }
            else {
                inStack.push(new Double(val2));
            }
        }
        catch(Exception e) {
            inStack.push(new Double(0));
        }
    }
    
}
