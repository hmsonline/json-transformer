package com.hmsonline.json.transformer;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * JSON transforming with condition
 * @author baotran
 *
 */
public class JsonRule implements JsonTransformer {
    private JsonRuleCondition cond;
    private JsonTransformer transformer;
    
    public JsonRule(JsonRuleCondition cond, JsonTransformer transformer) throws InvalidTransformerException {
        this.cond = cond;
        this.transformer = transformer;
    }
    
    /**
     * Transform the target field
     */
    public void transform(JSONObject root, Object targetKey) {
        transform(root.get(targetKey));
    }

    public void transform(Object target) {
        if (target instanceof JSONObject)
            this.transform((JSONObject)target);
        else if (target instanceof JSONArray)
            this.transform((JSONArray)target);
    }
    
    /**
     * Walk through fields and apply transforming if field meet condition
     * Examples:
     * Remove all PRICE field recursively
     *          { "PRICE>": "REMOVE" }
     * Remove CREATED_TIME in sub-elements under ORDER (keep TIME of ORDER) 
     *          {"ORDER": { "^CREATED_TIME": { "CREATED_TIME>": "REMOVE" }}}
     * @param root
     * @return
     */
    public void transform(JSONObject root) {
        Object[] keySet = root.keySet().toArray();
        for(Object k : keySet) {
            if (cond.check(k.toString())) {
                this.transformer.transform(root, k); 
            }
            
            // apply recursively
            if (cond.isRecursive()) {
                this.transform(root, k);
            }
        }
    }

    /**
     * Apply transform to each json object in the json array
     * @param root
     */
    private void transform(JSONArray root) {
        for (int i = 0, size = root.size(); i < size; i++)
        {
            Object target = root.get(i);
            if (target instanceof JSONObject)
                this.transform((JSONObject)target);
        }
    }
    
    public boolean check(String fieldName) {
        if (cond != null)
            return cond.check(fieldName);
        else
            return true;
    }
}
