package br.unicamp.ft.priva.models;

/**
 * Generalization Numeric Range - Model for numeric ranges
 */
public class GeneralizationNumericRange {

    int initialNumber;
    int finalNumber;
    String range;

    public int getInitialNumber() {
        return initialNumber;
    }

    public void setInitialNumber(int initialNumber) {
        this.initialNumber = initialNumber;
    }

    public int getFinalNumber() {
        return finalNumber;
    }

    public void setFinalNumber(int finalNumber) {
        this.finalNumber = finalNumber;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }
}
