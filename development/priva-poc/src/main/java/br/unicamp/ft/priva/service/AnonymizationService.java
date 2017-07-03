package br.unicamp.ft.priva.service;

import br.unicamp.ft.priva.models.Document;
import br.unicamp.ft.priva.models.Field;
import br.unicamp.ft.priva.models.Parameters;
import org.apache.log4j.Logger;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Anonymization Service
 */
public class AnonymizationService {

    public static final String SUPRESSION = "SUP";
    public static final String MASKING = "MAS";
    public static final String GENERALIZATION = "GEN";
    public static final String ENCRYPTION = "ENC";

    static final Logger logger = Logger.getLogger(AnonymizationService.class);

    /**
     * Anonymize Data
     */
    public List<Document> anonymizeData(List<Document> documentList, List<Parameters> paramList) {
        Set<String> setErrors = new HashSet<String>();

        for (Parameters params : paramList) {
            for (Document document : documentList) {
                for (Field field : document.getFields()) {

                    if (params.getFieldName() == null) {
                        setErrors.add("Invalid parameter");
                        break;
                    }

                    if (params.getFieldName().equals(field.getName())) {

                        if (params.getAnonymizationType().equals(SUPRESSION)) {
                            field.setAnonymization(new Supression());
                            // 
                        }

                        if (params.getAnonymizationType().substring(0, 3).equals(MASKING)) {
                            field.setAnonymization(new Masking());
                        }

                        if (params.getAnonymizationType().substring(0, 3).equals(GENERALIZATION)) {
                            field.setAnonymization(new Generalization());
                        }

                        if (params.getAnonymizationType().substring(0, 3).equals(ENCRYPTION)) {
                            field.setAnonymization(new Encryption());
                        }

                        if (null == field.getAnonymization()) {
                            setErrors.add(params.getAnonymizationType());
                        }

                        String anonymizedValue = "";

                        try {
                            anonymizedValue = field.getAnonymization().run(field.getValue(), params.getDetails());
                        } catch (Exception ex) {
                            anonymizedValue = field.getValue();
                        }

                        field.setValue(anonymizedValue);

                    }
                }
            }
        }

        for (String error : setErrors) {
            logger.warn("Invalid anonymization type: " + error);
        }

        return documentList;
        //printAnonymizedData(documentList);

    }

    private void printAnonymizedData(List<Document> documentList) {
        System.out.println("\n * ANONYMIZED DATA * \n");
        for (Document document : documentList) {
            for (Field field : document.getFields()) {
                System.out.print(field.getName() + ": " + field.getValue() + "\n");
            }
            System.out.println("--------------------------------------------------------------");
        }
    }

}
