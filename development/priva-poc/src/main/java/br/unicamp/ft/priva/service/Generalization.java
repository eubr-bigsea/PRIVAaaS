package br.unicamp.ft.priva.service;

import br.unicamp.ft.priva.models.GeneralizationNumericRange;
import br.unicamp.ft.priva.models.GeneralizationTextRange;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Anonymization Service
 */
public class Generalization implements AnonymizationInterface {

    /**
     * Run generalization technique
     */
    public String run(String value, String detail) {
        if (detail.startsWith("[")) {
            return generalizeNumericValue(value, getNumericRange(detail));
        }

        if (detail.startsWith("{")) {
            return generalizeTextValue(value, getTextRange(detail));
        } else {
            System.out.print("Invalid range");
            return value;
        }
    }

    private String generalizeNumericValue(String value, List<GeneralizationNumericRange> numericRange) {

        int intValue = Integer.parseInt(value);

        for (GeneralizationNumericRange range : numericRange) {
            if (intValue >= range.getInitialNumber() && intValue < range.getFinalNumber()) {
                return range.getRange();
            }
        }

        return "";

    }

    private List<GeneralizationNumericRange> getNumericRange(String detail) {

        String[] parts = detail.split(";");

        List<GeneralizationNumericRange> listaGen = new ArrayList<GeneralizationNumericRange>();

        for (int i = 0; i <= parts.length - 1; i++) {

            parts[i] = parts[i].replace("[", "");
            parts[i] = parts[i].replace("]", "");

            String[] parts2 = parts[i].split("-");

            GeneralizationNumericRange g = new GeneralizationNumericRange();
            g.setInitialNumber(Integer.parseInt(parts2[0]));

            int indexString = parts2[1].indexOf("=");

            if (parts2[1].substring(0, indexString).equals("x")) {
                g.setFinalNumber(Integer.MAX_VALUE);
            } else {
                g.setFinalNumber(Integer.parseInt(parts2[1].substring(0, indexString)));
            }

            g.setRange(parts2[1].substring(indexString + 1, parts2[1].length()));

            listaGen.add(g);

        }

        return listaGen;

    }

    private List<GeneralizationTextRange> getTextRange(String detail) {
        String[] str = detail.split(";");

        List<GeneralizationTextRange> listGeneralization = new ArrayList<GeneralizationTextRange>();

        for (int i = 0; i <= str.length - 1; i++) {

            GeneralizationTextRange generalization = new GeneralizationTextRange();

            str[i] = str[i].replace("{", "");
            str[i] = str[i].replace("}", "");

            int index = str[i].indexOf("=");
            String tempString = str[i];
            generalization.setRange(tempString.substring(index + 1, str[i].length()));
            String[] lista = str[i].substring(0, index).split(",");

            generalization.setRangeValues(Arrays.asList(lista));

            listGeneralization.add(generalization);

        }

        return listGeneralization;
    }

    private String generalizeTextValue(String value, List<GeneralizationTextRange> textRange) {

        for (GeneralizationTextRange range : textRange) {
            if (range.getRangeValues().contains(value)) {
                return range.getRange();
            }
        }

        return "";

    }

}
