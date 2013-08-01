package com.hmsonline.json.transformer;

import java.util.ArrayList;

import org.json.simple.JSONObject;

/**
 * Chain of JsonRules
 * @author baotran
 *
 */
public class JsonRuleList implements JsonTransformer {
    private ArrayList<JsonRule> transformers = null;
    
    public JsonRuleList() {
        this.transformers = new ArrayList<JsonRule>();
    }
    
    public void addRule(JsonRule rule) {
        this.transformers.add(rule);
    }
    
    public void transform(JSONObject parent, Object targetKey) {
        for(JsonTransformer r : this.transformers)
            r.transform(parent, targetKey);
    }
    
    public void transform(Object root) {
        for(JsonTransformer r : this.transformers)
            r.transform(root);
    }
}
