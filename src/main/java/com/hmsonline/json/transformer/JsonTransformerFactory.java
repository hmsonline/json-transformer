package com.hmsonline.json.transformer;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class JsonTransformerFactory {
    static public JsonTransformer factory(String json) throws InvalidTransformerException {
        return factory(JSONValue.parse(json));
    }    
    
    static public JsonRule factory(JSONObject json) throws InvalidTransformerException {
        if (json.keySet().size() != 1) {
            throw new InvalidTransformerException(json.toString());
        }
        
        String key = json.keySet().iterator().next().toString();
        Object value = json.get(key);

        JsonRuleCondition cond = new JsonRuleCondition(key);
        JsonTransformer transformer = factory(value);;
        
        return new JsonRule(cond, transformer);
    }    
    
    static public JsonRuleList factory(JSONArray jsonArray) throws InvalidTransformerException {
        JsonRuleList rules = new JsonRuleList();
        for (int i = 0, size = jsonArray.size(); i < size; i++)
        {
            JSONObject json = (JSONObject)jsonArray.get(i);
            rules.addRule(factory(json));
        }
        return rules;
    }   
    
    static public JsonTransformer factoryAction(String action) throws InvalidTransformerException {
        if (action.equals("REMOVE")) {
            return new JsonRemoveAction();
        }
        else if (action.startsWith("LIMIT ")) {
            return new JsonLimitAction(action.substring(6));
        }
        else {
            throw new InvalidTransformerException("Not supported transformer action: " + action);
        }
    }
    
    //Convenient private method
    static private JsonTransformer factory(Object jsonObject) throws InvalidTransformerException {
        if (jsonObject instanceof JSONObject) {
            return factory((JSONObject)jsonObject);
        }
        else if (jsonObject instanceof JSONArray) {
            return factory((JSONArray)jsonObject);
        }
        else if (jsonObject instanceof String) {
            return factoryAction((String)jsonObject);
        }
        
        throw new InvalidTransformerException("Not supported transformer type: " + jsonObject);
    }
}
