# Project Definition

Provide a library that supports transformation of an input JSON through declarative rules contained in a separate JSON.  The initial capability is limited to filtering out fields by name or by limiting the number of values in a list.  There is also the capability to reverse the condition of a rule and to apply rules recursively to nested nodes.

----
## Syntax
 
The syntax for transformation rules is defined below:

```
RULE:=
{
    "[^]<FIELD_NAME>[>]" : <ACTION> | <RULE> | <RULE_LIST>
}
```
```
ACTION:= "REMOVE" | "LIMIT <NUMBER>"
```
```
RULE_LIST:= [<RULE>, <RULE>, ...]
```

### Additional syntax notes:

* `RULE_LIST` is a JSON array containing a list of `<RULE>`s.

Optional prefix and suffix values can be applied to the `<FIELD_NAME>` as defined shown below.  These actions apply to the node where the prefix or suffix is entered.

* “^” prefix is used to reverse the condition defined by the `<ACTION>` associated with the `<FIELD_NAME>`
 
* “>” suffix is used to apply the `<ACTION>` associated with the `<FIELD_NAME>` recursively to nested nodes

----
## Examples

The input JSON being used is the transformation examples below is here:

https://github.com/hmsonline/json-transformer/blob/master/src/test/resources/source.json

* Select ORDER node, remove ORDER_ID wherever it occurs

```
{"ORDER": [
        { "ORDER_ID" : "REMOVE" }
]}
```

* Select ORDER node, remove ORDER_ID for all sub-elements under an ORDER (keeping top level ORDER_ID)

```
{"ORDER": [
        { "^ORDER_ID": { "ORDER_ID>": "REMOVE" }},
]}
```

* Select ORDER node, keep only one ORDER_ITEM per ORDER

```
{"ORDER": [
        { "ORDER_LIST>" : "LIMIT 1" }
]}
```

* Apply multiple rules:
** Select ORDER node
** Remove ORDER_ID for all sub-elements under an ORDER (keeping top level ORDER_ID)
** Remove CREATED_DATE wherever it occurs
** Keep only one ORDER_ITEM per ORDER

```
{"ORDER": [
        { "^ORDER_ID": { "ORDER_ID>": "REMOVE" }},
        { "CREATED_DATE>" : "REMOVE" },
        { "ORDER_LIST>" : "LIMIT 1" }
]}
```

----
## Usage

Use the following commands to run the example:

```
$ mvn clean install
$ cd example; mvn clean install;
$ mvn exec:java -Dexec.args="./source.json ./rule.json"
```
