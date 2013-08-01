package com.hmsonline.json.transformer;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Apply on JSONArray, reduce size to limit set
 * @author baotran
 *
 */
public class JsonLimitAction implements JsonTransformer {
    private int limit;
    
    public JsonLimitAction(String sLimit) {
        this.limit = Integer.parseInt(sLimit);
    }
    
    public void transform(JSONObject parent, Object targetKey) {
        if (parent != null) {
            transform(parent.get(targetKey));
        }
    }

    public void transform(Object target) {
        if (target != null 
            && target instanceof JSONArray) {
            JSONArray arr = (JSONArray) target;
            if (arr.size() > limit) {
                for(int i = arr.size() -1; i >= limit; i--)
                    arr.remove(i);
            }
        }
    }
}
