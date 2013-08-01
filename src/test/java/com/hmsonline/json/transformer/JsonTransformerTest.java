package com.hmsonline.json.transformer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.FileReader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.junit.Before;
import org.junit.Test;

public class JsonTransformerTest {
    public JSONObject sourceJson = null;
    
    @Before
    public void prepare() throws FileNotFoundException {
        sourceJson = (JSONObject)JSONValue.parse(
                new FileReader(JsonTransformerTest.class.getResource("/source.json").getFile()));
    }
    
    @Test
    public void testSimple() throws InvalidTransformerException {
        JSONObject source =(JSONObject)JSONValue.parse("{\"ORDER\": {\"ORDER_ID\": \"ORDER_123\", " +
        		"\"BILLING_ADDRESS\": [{\"ORDER_ID\": \"ORDER_123\"}]}}");
        
        JsonRule rule = (JsonRule)JsonTransformerFactory.factory("{\"ORDER\" :{\"BILLING_ADDRESS\": \"REMOVE\"}}");
        assertEquals(true, rule.check("ORDER"));
        assertEquals(true, rule.check("order"));
        assertEquals(false, rule.check("BILLING_ADDRESS"));
        assertEquals(false, rule.check("billing_address"));
        
        rule.transform(source);
        assertEquals(-1, source.toString().indexOf("BILLING_ADDRESS"));
    }
    
    @Test
    public void testRemoveRecursively() throws InvalidTransformerException {
        JsonTransformer rule = JsonTransformerFactory.factory("{ \"ORDER_ID>\": \"REMOVE\" }");
        rule.transform(sourceJson);

        assertEquals(-1, sourceJson.toString().indexOf("ORDER_ID"));
    }
    
    @Test
    public void testRemoveSubElements() throws InvalidTransformerException {
        JsonTransformer rule = JsonTransformerFactory.factory(
                "{" +
                "       \"ORDER\": {" +
                "               \"^ORDER_ID\": " +
                "                       { \"ORDER_ID>\": \"REMOVE\" }" +
                "       }" +
                "}");
        rule.transform(sourceJson);
        String transformed = sourceJson.toString();

        int foundOne = transformed.indexOf("ORDER_ID");
        assertTrue(foundOne > 0);
        assertTrue(transformed.indexOf("ORDER_ID", foundOne+1) == -1);
    }

    @Test
    public void testRuleList() throws InvalidTransformerException {
        JsonTransformer rule = JsonTransformerFactory.factory(
                "{\"ORDER\": [" +
                "        { \"^ORDER_ID\": { \"ORDER_ID>\": \"REMOVE\" }}," +
                "        { \"CREATED_TIME>\" : \"REMOVE\" }" +
                "]}");
        rule.transform(sourceJson);
        String transformed = sourceJson.toString();
        
        int foundOne = transformed.indexOf("ORDER_ID");
        assertTrue(foundOne > 0);
        assertTrue(transformed.indexOf("ORDER_ID", foundOne+1) == -1);
        assertEquals(-1, transformed.indexOf("CREATED_TIME"));
    }
    
    @Test
    public void testLimitSize() throws InvalidTransformerException {
        JSONObject source =(JSONObject)JSONValue.parse("{ \"ORDER_LIST\": [{\"ORDER_ITEM\":\"1\"}, {\"ORDER_ITEM\":\"2\"}, {\"ORDER_ITEM\":\"3\"}] }");
        JsonTransformer rule = JsonTransformerFactory.factory("{\"ORDER_LIST\": \"LIMIT 2\"}");
        
        assertTrue(((JSONArray)source.get("ORDER_LIST")).size() == 3);
        rule.transform(source);
        assertTrue(((JSONArray)source.get("ORDER_LIST")).size() == 2);
    }
}
