package com.hmsonline.json.transformer;

import org.json.simple.JSONObject;

/**
 * Remove a node from json
 * @author baotran
 *
 */
public class JsonRemoveAction implements JsonTransformer {
    public void transform(JSONObject parent, Object targetKey) {
        parent.remove(targetKey);
    }

    public void transform(Object jsonObject) {
        //do nothing
    }
}
