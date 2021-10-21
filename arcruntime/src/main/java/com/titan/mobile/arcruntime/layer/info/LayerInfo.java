/**
 * Copyright 2019 bejson.com
 */
package com.titan.mobile.arcruntime.layer.info;

import java.io.Serializable;
import java.util.List;

/**
 * Auto-generated: 2019-06-04 14:6:24
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class LayerInfo implements Serializable {

    private double currentVersion;
    private int id;
    private String name;
    private String type;
    private String description;
    private String geometryType;
    private String copyrightText;
    private ParentLayer parentLayer;
    private List<String> subLayers;
    private Double minScale;
    private Double maxScale;
    private DrawingInfo drawingInfo;
    private boolean defaultVisibility;
    private Extent extent;
    private boolean hasAttachments;
    private String htmlPopupType;
    private String displayField;
    private String typeIdField;
    private List<Field> fields;
    private List<String> relationships;
    private boolean canModifyLayer;
    private boolean canScaleSymbols;
    private boolean hasLabels;
    private String capabilities;
    private int maxRecordCount;
    private boolean supportsStatistics;
    private boolean supportsAdvancedQueries;
    private String supportedQueryFormats;
    private OwnershipBasedAccessControlForFeatures ownershipBasedAccessControlForFeatures;
    private boolean useStandardizedQueries;
    private AdvancedQueryCapabilities advancedQueryCapabilities;

    public void setCurrentVersion(double currentVersion) {
        this.currentVersion = currentVersion;
    }

    public double getCurrentVersion() {
        return currentVersion;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

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

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setGeometryType(String geometryType) {
        this.geometryType = geometryType;
    }

    public String getGeometryType() {
        return geometryType;
    }

    public void setCopyrightText(String copyrightText) {
        this.copyrightText = copyrightText;
    }

    public String getCopyrightText() {
        return copyrightText;
    }

    public void setParentLayer(ParentLayer parentLayer) {
        this.parentLayer = parentLayer;
    }

    public ParentLayer getParentLayer() {
        return parentLayer;
    }

    public void setSubLayers(List<String> subLayers) {
        this.subLayers = subLayers;
    }

    public List<String> getSubLayers() {
        return subLayers;
    }

    public void setMinScale(Double minScale) {
        this.minScale = minScale;
    }

    public Double getMinScale() {
        return minScale;
    }

    public void setMaxScale(Double maxScale) {
        this.maxScale = maxScale;
    }

    public Double getMaxScale() {
        return maxScale;
    }

    public void setDrawingInfo(DrawingInfo drawingInfo) {
        this.drawingInfo = drawingInfo;
    }

    public DrawingInfo getDrawingInfo() {
        return drawingInfo;
    }

    public void setDefaultVisibility(boolean defaultVisibility) {
        this.defaultVisibility = defaultVisibility;
    }

    public boolean getDefaultVisibility() {
        return defaultVisibility;
    }

    public void setExtent(Extent extent) {
        this.extent = extent;
    }

    public Extent getExtent() {
        return extent;
    }

    public void setHasAttachments(boolean hasAttachments) {
        this.hasAttachments = hasAttachments;
    }

    public boolean getHasAttachments() {
        return hasAttachments;
    }

    public void setHtmlPopupType(String htmlPopupType) {
        this.htmlPopupType = htmlPopupType;
    }

    public String getHtmlPopupType() {
        return htmlPopupType;
    }

    public void setDisplayField(String displayField) {
        this.displayField = displayField;
    }

    public String getDisplayField() {
        return displayField;
    }

    public void setTypeIdField(String typeIdField) {
        this.typeIdField = typeIdField;
    }

    public String getTypeIdField() {
        return typeIdField;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setRelationships(List<String> relationships) {
        this.relationships = relationships;
    }

    public List<String> getRelationships() {
        return relationships;
    }

    public void setCanModifyLayer(boolean canModifyLayer) {
        this.canModifyLayer = canModifyLayer;
    }

    public boolean getCanModifyLayer() {
        return canModifyLayer;
    }

    public void setCanScaleSymbols(boolean canScaleSymbols) {
        this.canScaleSymbols = canScaleSymbols;
    }

    public boolean getCanScaleSymbols() {
        return canScaleSymbols;
    }

    public void setHasLabels(boolean hasLabels) {
        this.hasLabels = hasLabels;
    }

    public boolean getHasLabels() {
        return hasLabels;
    }

    public void setCapabilities(String capabilities) {
        this.capabilities = capabilities;
    }

    public String getCapabilities() {
        return capabilities;
    }

    public void setMaxRecordCount(int maxRecordCount) {
        this.maxRecordCount = maxRecordCount;
    }

    public int getMaxRecordCount() {
        return maxRecordCount;
    }

    public void setSupportsStatistics(boolean supportsStatistics) {
        this.supportsStatistics = supportsStatistics;
    }

    public boolean getSupportsStatistics() {
        return supportsStatistics;
    }

    public void setSupportsAdvancedQueries(boolean supportsAdvancedQueries) {
        this.supportsAdvancedQueries = supportsAdvancedQueries;
    }

    public boolean getSupportsAdvancedQueries() {
        return supportsAdvancedQueries;
    }

    public void setSupportedQueryFormats(String supportedQueryFormats) {
        this.supportedQueryFormats = supportedQueryFormats;
    }

    public String getSupportedQueryFormats() {
        return supportedQueryFormats;
    }

    public void setOwnershipBasedAccessControlForFeatures(OwnershipBasedAccessControlForFeatures ownershipBasedAccessControlForFeatures) {
        this.ownershipBasedAccessControlForFeatures = ownershipBasedAccessControlForFeatures;
    }

    public OwnershipBasedAccessControlForFeatures getOwnershipBasedAccessControlForFeatures() {
        return ownershipBasedAccessControlForFeatures;
    }

    public void setUseStandardizedQueries(boolean useStandardizedQueries) {
        this.useStandardizedQueries = useStandardizedQueries;
    }

    public boolean getUseStandardizedQueries() {
        return useStandardizedQueries;
    }

    public void setAdvancedQueryCapabilities(AdvancedQueryCapabilities advancedQueryCapabilities) {
        this.advancedQueryCapabilities = advancedQueryCapabilities;
    }

    public AdvancedQueryCapabilities getAdvancedQueryCapabilities() {
        return advancedQueryCapabilities;
    }

}