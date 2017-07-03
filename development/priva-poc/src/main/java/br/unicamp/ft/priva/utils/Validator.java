package br.unicamp.ft.priva.utils;

import java.io.File;

public class Validator {

    public static boolean validatePaths(String param, String data) {
        boolean isValid = true;
        File paramPath = new File(param);
        File dataPath = new File(data);
        String paramError = "";
        String dataError = "";

        if (!paramPath.exists()) {
            paramError = "The path " + "\'" + param + "\'" + " could not be found!\n";
            isValid = false;
        }

        if (!dataPath.exists()) {
            dataError = "The path " + "\'" + data + "\'" + " could not be found!\n";
            isValid = false;
        }

        String paramName = paramPath.getName().toUpperCase();
        String dataName = dataPath.getName().toUpperCase();

        if (!dataName.endsWith(".JSON")) {
            dataError = dataError.concat("\'" + data + "\'" + " is not a valid JSON file!\n");
            isValid = false;
        }

        if (!paramName.endsWith(".JSON")) {
            paramError = paramError.concat("\'" + param + "\'" + " is not a valid JSON file!\n");
            isValid = false;
        }

        if (!isValid) {
            System.out.println("\nThe following errors occured:\n");
            System.out.print(dataError);
            System.out.print(paramError);
        }

        return isValid;
    }
}
