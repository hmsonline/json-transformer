A json-to-json transform library
================

The purpose is to build a library supports transforming a json by declarative rules defined in json.

For now the capability is only limitted to filtering fields in json.
 
Transformation rule is defined as below syntax

```
RULE:=
{
    "<SELECTED_NAME>" : <ACTION> | <RULE> | <RULE_LIST>
}

ACTION:= "REMOVE" | "LIMIT <NUMBER>"

RULE_LIST:= [<RULE>, <RULE>, ...]
```

Example: Below rule will filter out these things
  - Remove ORDER_ID for all sub-elements under a ORDER.
  - Remove CREATED_DATE wherever it occurs
  - Only keep 1 ORDER_ITEM per ORDER

```
{"ORDER": [
        { "^ORDER_ID": { "ORDER_ID>": "REMOVE" }},
        { "CREATED_DATE>" : "REMOVE" },
        { "ORDER_LIST>" : "LIMIT 1" }
]}
```

Usage: to run example
```
$ mvn clean install
$ cd example; mvn clean install;
$ mvn exec:java -Dexec.args="./source.json ./rule.json"
```

Rule syntax:
* FIELD_NAME: name of node wish to apply action
  - “^” prefix can be used to reverse condition
  - “>” suffix is used to apply recursively to nested nodes

  Examples:
  - “ORDER” : select ORDER node
  - “^ORDER_ID”: select all nodes except ORDER_ID
  - “ORDER_ID>”: recursively select ORDER_ID of current node and its child nodes.
* ACTION: currently only "REMOVE" and "LIMIT <N>" are supported
  - Remove all ORDER_ID of root node and recursively apply this rule to all child nodes. As a result, ORDER_ID will be removed whether it occurs.
    ```{ "ORDER_ID>": "REMOVE" }```
  - Remove ORDER_ID in all sub-elements under ORDER
    ```{"ORDER": { "^ORDER_ID": { "ORDER_ID>": "REMOVE" }}}```
  - Keep only 3 items of ORDER_LIST:
    ```{ "ORDER_LIST>" : "LIMIT 3" }```
* RULE_LIST: json array contains list of rules.
