package com.titan.mobile.arcruntime.layer.info;

import java.io.Serializable;
import java.util.List;

public class Domain implements Serializable {
    private String type;

    private String name;

    private List<CodedValues> codedValues ;

    public void setType(String type){
        this.type = type;
    }
    public String getType(){
        return this.type;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void setCodedValues(List<CodedValues> codedValues){
        this.codedValues = codedValues;
    }
    public List<CodedValues> getCodedValues(){
        return this.codedValues;
    }
}
