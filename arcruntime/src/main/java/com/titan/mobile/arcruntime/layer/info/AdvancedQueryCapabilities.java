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
public class AdvancedQueryCapabilities implements Serializable {

    private boolean useStandardizedQueries;
    private boolean supportsStatistics;
    private boolean supportsOrderBy;
    private boolean supportsDistinct;
    private boolean supportsPagination;
    private boolean supportsTrueCurve;

    public void setUseStandardizedQueries(boolean useStandardizedQueries) {
        this.useStandardizedQueries = useStandardizedQueries;
    }

    public boolean getUseStandardizedQueries() {
        return useStandardizedQueries;
    }

    public void setSupportsStatistics(boolean supportsStatistics) {
        this.supportsStatistics = supportsStatistics;
    }

    public boolean getSupportsStatistics() {
        return supportsStatistics;
    }

    public void setSupportsOrderBy(boolean supportsOrderBy) {
        this.supportsOrderBy = supportsOrderBy;
    }

    public boolean getSupportsOrderBy() {
        return supportsOrderBy;
    }

    public void setSupportsDistinct(boolean supportsDistinct) {
        this.supportsDistinct = supportsDistinct;
    }

    public boolean getSupportsDistinct() {
        return supportsDistinct;
    }

    public void setSupportsPagination(boolean supportsPagination) {
        this.supportsPagination = supportsPagination;
    }

    public boolean getSupportsPagination() {
        return supportsPagination;
    }

    public void setSupportsTrueCurve(boolean supportsTrueCurve) {
        this.supportsTrueCurve = supportsTrueCurve;
    }

    public boolean getSupportsTrueCurve() {
        return supportsTrueCurve;
    }

}