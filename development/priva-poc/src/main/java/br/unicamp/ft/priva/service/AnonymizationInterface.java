package br.unicamp.ft.priva.service;

/**
 * Anonymization Interface
 */
public interface AnonymizationInterface {

    /**
     * Each anonymization will have this method
     */
    String run(String value, String detail);

}
