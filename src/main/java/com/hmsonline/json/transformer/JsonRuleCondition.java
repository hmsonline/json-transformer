package com.hmsonline.json.transformer;

public class JsonRuleCondition {
    private String field;
    private boolean neg = false;
    private boolean recur = false;
    
    public JsonRuleCondition(String cond) {
        if (cond.startsWith("^")) {
            this.neg = true;
            cond = cond.substring(1);
        }
        
        if (cond.endsWith(">")) {
            this.recur = true;
            cond = cond.substring(0, cond.length()-1);
        }
        
        this.field = cond;
    }
    
    public boolean check(String fieldName) {
        boolean result = this.field.equalsIgnoreCase(fieldName); 
        
        if (neg) 
            return !result;
        else 
            return result;
    }
    
    public boolean isRecursive() {
        return this.recur;
    }
}
