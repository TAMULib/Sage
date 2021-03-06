package edu.tamu.sage.model;

import javax.persistence.Embeddable;

@Embeddable
public class SearchField {

    private String key;

    private String label;

    public SearchField() {
        super();
    }

    public SearchField(String key, String label) {
        super();
        this.key = key;
        this.label = label;
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

}
