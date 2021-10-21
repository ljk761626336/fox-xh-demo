package com.titan.mobile.arcruntime.layer.info; /**
  * Copyright 2020 bejson.com 
  */

import java.io.Serializable;

/**
 * Auto-generated: 2020-04-09 10:29:41
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Font implements Serializable {

    private String family;
    private int size;
    private String style;
    private String weight;
    private String decoration;
    public void setFamily(String family) {
         this.family = family;
     }
     public String getFamily() {
         return family;
     }

    public void setSize(int size) {
         this.size = size;
     }
     public int getSize() {
         return size;
     }

    public void setStyle(String style) {
         this.style = style;
     }
     public String getStyle() {
         return style;
     }

    public void setWeight(String weight) {
         this.weight = weight;
     }
     public String getWeight() {
         return weight;
     }

    public void setDecoration(String decoration) {
         this.decoration = decoration;
     }
     public String getDecoration() {
         return decoration;
     }

}