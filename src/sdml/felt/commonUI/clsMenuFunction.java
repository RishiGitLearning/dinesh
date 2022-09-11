/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdml.felt.commonUI;

import java.util.HashMap;
/**
 *
 * @author  nrpatel
 * @version 
 */

public class clsMenuFunction {
    private HashMap props;    
    
    public Variant getAttribute(String PropName) {
        return (Variant) props.get(PropName);
    }
    
    public void setAttribute(String PropName,Object Value) {
        props.put(PropName,new Variant(Value));
    }
    
    public void setAttribute(String PropName,int Value) {
        props.put(PropName,new Variant(Value));
    }
    
    public void setAttribute(String PropName,long Value) {
        props.put(PropName,new Variant(Value));
    }
        
    public void setAttribute(String PropName,boolean Value) {
        props.put(PropName,new Variant(Value));
    }
        
    /** Creates new clsMenuFunctionObject */
    public clsMenuFunction() {
        props=new HashMap();    
        props.put("COMPANY_ID",new Variant(0));
        props.put("SR_NO",new Variant(0));
        props.put("MENU_ID",new Variant(0));
        props.put("FUNCTION_ID",new Variant(0));
        props.put("FUNCTION_NAME",new Variant(""));
    }

}
