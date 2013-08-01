package com.hmsonline.json.transformer;

import org.json.simple.JSONObject;

/**
 * Interface for json transform action with or w/o condition
 * @author baotran
 */
public interface JsonTransformer {
    void transform(JSONObject parent, Object targetKey);
    
    void transform(Object jsonObject);
}
