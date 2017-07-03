package br.unicamp.ft.priva.models;

import java.util.List;

/**
 * Generalization Text Range - Model for text ranges
 */
public class GeneralizationTextRange {

    List<String> rangeValues;
    String range;

    public List<String> getRangeValues() {
        return rangeValues;
    }

    public void setRangeValues(List<String> rangeValues) {
        this.rangeValues = rangeValues;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }
}
