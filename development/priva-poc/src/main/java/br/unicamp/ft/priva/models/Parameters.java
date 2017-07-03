package br.unicamp.ft.priva.models;

import java.util.List;

/**
 * Parameters model - class responsible to store the parameters
 */
public class Parameters {

    String fieldName;
    String anonymizationType;
    String details;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getAnonymizationType() {
        return anonymizationType;
    }

    public void setAnonymizationType(String anonymizationType) {
        this.anonymizationType = anonymizationType;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
