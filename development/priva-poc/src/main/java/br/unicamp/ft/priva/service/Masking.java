package br.unicamp.ft.priva.service;

public class Masking implements AnonymizationInterface {

    /**
     * Run Masking technique
     */
    public String run(String value, String detail) {
        MockMasking mock = new MockMasking();

        if ("".equals(detail) || detail == null) {
            return mock.getFullName();
        }

        if (detail.equals("FULL")) {
            return mock.getFullName();
        }

        if (detail.equals("LAST")) {
            return mock.getLastName();
        }

        if (detail.equals("FIRST")) {
            return mock.getName();
        } else {
            return value;
        }
    }
}
