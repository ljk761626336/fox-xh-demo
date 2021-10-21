/**
 * Copyright 2019 bejson.com
 */
package com.titan.mobile.arcruntime.layer.info;

import java.io.Serializable;

/**
 * Auto-generated: 2019-06-04 14:6:24
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Field implements Serializable {

    private String name;
    private String type;
    private String alias;
    private Domain domain;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getAlias() {
        return alias;
    }

    public Domain getDomain() {
        return domain;
    }

    public void setDomain(Domain domain) {
        this.domain = domain;
    }
}