package com.hmsonline.json.transformer;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.File;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
* Sample of usage
**/
public class TransformerTest {
    static public void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println("Usage: mvn exec:java  -Dexec.args=\"<source file> <rule file>\"\n");
            System.out.println("Example: mvn exec:java  -Dexec.args=\"./source.json ./rule.json\"\n");
        }
        else {
            JSONObject json = (JSONObject)JSONValue.parse(new FileReader(new File(args[0])));
            JSONObject jsonRule = (JSONObject)JSONValue.parse(new FileReader(new File(args[1])));
            JsonRule rule = (JsonRule)JsonTransformerFactory.factory(jsonRule);
            
            System.out.println("Source: \n" + json.toString());
            System.out.println("Rule: \n" + jsonRule.toString());
            rule.transform(json);
            System.out.println("Result: \n" + json.toString());
        }
    }
}
