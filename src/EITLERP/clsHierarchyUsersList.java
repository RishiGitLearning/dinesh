/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP;

import java.util.HashMap;

/**
 *
 * @author root
 */
public class clsHierarchyUsersList {
    private HashMap props;    
    public boolean Ready = false;

    public HashMap colFieldAccess=new HashMap();
    
    public Variant getAttribute(String PropName)
    {
        return (Variant) props.get(PropName);
    }
    
    public void setAttribute(String PropName,Object Value)
    {
         props.put(PropName,new Variant(Value));
    }

    public void setAttribute(String PropName,int Value)
    {
         props.put(PropName,new Variant(Value));
    }
    
    public void setAttribute(String PropName,long Value)
    {
         props.put(PropName,new Variant(Value));
    }

    public void setAttribute(String PropName,double Value)
    {
         props.put(PropName,new Variant(Value));
    }

    public void setAttribute(String PropName,float Value)
    {
         props.put(PropName,new Variant(Value));
    }

    public void setAttribute(String PropName,boolean Value)
    {
         props.put(PropName,new Variant(Value));
    }

    
    /** Creates new clsNoDataObject */
    public clsHierarchyUsersList() {
        props=new HashMap();  
        
        props.put("MODULE_ID",new Variant(0));
        props.put("IS_DEFAULT",new Variant(false));
        props.put("HIERARCHY_NAME",new Variant(""));
        
        props.put("COMPANY_ID",new Variant(0));
        props.put("HIERARCHY_ID",new Variant(0));
        props.put("USER_ID",new Variant(0));
        props.put("SR_NO",new Variant(0));
        props.put("APPROVER",new Variant(false));
        props.put("FINAL_APPROVER",new Variant(false));
        props.put("CREATOR",new Variant(false));
        props.put("APPROVAL_SEQUENCE",new Variant(0));
        props.put("SKIP_SEQUENCE",new Variant(false));
        props.put("GRANT_OTHER",new Variant(false));
        props.put("FROM_DATE",new Variant(""));
        props.put("TO_DATE",new Variant(""));
        props.put("GRANT_USER_ID",new Variant(""));
        props.put("RESTORE",new Variant(false));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
    }
    
}
