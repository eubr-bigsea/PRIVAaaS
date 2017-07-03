package br.unicamp.ft.priva.models;

import java.util.List;

/**
 * Document Model - Class responsible for store data
 */
public class Document {

    private List<Field> fields;

    public Document() {
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }
}
