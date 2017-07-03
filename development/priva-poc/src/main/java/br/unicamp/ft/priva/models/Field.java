package br.unicamp.ft.priva.models;

import br.unicamp.ft.priva.service.AnonymizationInterface;

/**
 * Field Model
 */
public class Field {

    private String name;
    private String value;
    private AnonymizationInterface anonymization;

    public Field() {
    }

    public Field(String name, String value, AnonymizationInterface anonymization) {
        this.name = name;
        this.value = value;
        this.anonymization = anonymization;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public AnonymizationInterface getAnonymization() {
        return anonymization;
    }

    public void setAnonymization(AnonymizationInterface anonymization) {
        this.anonymization = anonymization;
    }
}
