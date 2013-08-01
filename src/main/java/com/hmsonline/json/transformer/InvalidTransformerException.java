package com.hmsonline.json.transformer;

public class InvalidTransformerException extends Exception {
    private static final long serialVersionUID = -1787568223055173106L;

    public InvalidTransformerException(String msg){
        super(msg);
    }

    public InvalidTransformerException(String msg, Throwable e){
        super(msg, e);
    }
}
