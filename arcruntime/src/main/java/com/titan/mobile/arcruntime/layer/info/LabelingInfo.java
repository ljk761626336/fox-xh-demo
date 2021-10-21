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
public class LabelingInfo implements Serializable {

    private String labelPlacement;
    private String where;
    private String labelExpression;
    private boolean useCodedValues;
    private Symbol symbol;
    private int minScale;
    private int maxScale;
    public void setLabelPlacement(String labelPlacement) {
         this.labelPlacement = labelPlacement;
     }
     public String getLabelPlacement() {
         return labelPlacement;
     }

    public void setWhere(String where) {
         this.where = where;
     }
     public String getWhere() {
         return where;
     }

    public void setLabelExpression(String labelExpression) {
         this.labelExpression = labelExpression;
     }
     public String getLabelExpression() {
         return labelExpression;
     }

    public void setUseCodedValues(boolean useCodedValues) {
         this.useCodedValues = useCodedValues;
     }
     public boolean getUseCodedValues() {
         return useCodedValues;
     }

    public void setSymbol(Symbol symbol) {
         this.symbol = symbol;
     }
     public Symbol getSymbol() {
         return symbol;
     }

    public void setMinScale(int minScale) {
         this.minScale = minScale;
     }
     public int getMinScale() {
         return minScale;
     }

    public void setMaxScale(int maxScale) {
         this.maxScale = maxScale;
     }
     public int getMaxScale() {
         return maxScale;
     }

}