package edu.tamu.sage.model;

import javax.persistence.Embeddable;

@Embeddable
public class MetadataField {

    private String key;

    private String label;

    private String type;

    private boolean sortable;

    private boolean inList;

    private boolean inGrid;

    private boolean inSingleResult;

    public MetadataField() {
        super();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isSortable() {
        return sortable;
    }

    public void setSortable(boolean sortable) {
        this.sortable = sortable;
    }

    public boolean isInList() {
        return inList;
    }

    public void setInList(boolean inList) {
        this.inList = inList;
    }

    public boolean isInGrid() {
        return inGrid;
    }

    public void setInGrid(boolean inGrid) {
        this.inGrid = inGrid;
    }

    public boolean isInSingleResult() {
        return inSingleResult;
    }

    public void setInSingleResult(boolean inSingleResult) {
        this.inSingleResult = inSingleResult;
    }

}
