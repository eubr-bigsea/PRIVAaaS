package br.unicamp.ft.priva.service;

/**
 * Created by andre on 05/12/2016.
 */
public class Supression implements AnonymizationInterface {

    /**
     * Run Supression
     */
    public String run(String value, String detail) {
        if ("".equals(detail) || detail == null) {
            return "*";
        } else {
            return detail;
        }
    }

}
