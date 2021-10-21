package com.titan.mobile.arcruntime.layer.info;

import java.io.Serializable;

public class CodedValues implements Serializable {
    private String name;

    private String code;

    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void setCode(String code){
        this.code = code;
    }
    public String getCode(){
        return this.code;
    }
}
