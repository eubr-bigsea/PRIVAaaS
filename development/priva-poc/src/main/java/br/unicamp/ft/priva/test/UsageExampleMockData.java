package br.unicamp.ft.priva.test;

import br.unicamp.ft.priva.models.Document;
import br.unicamp.ft.priva.models.Parameters;
import br.unicamp.ft.priva.service.AnonymizationService;
import br.unicamp.ft.priva.utils.Writer;
import br.unicamp.ft.priva.utils.Parser;
import br.unicamp.ft.priva.utils.Validator;

import java.util.List;

public class UsageExampleMockData {

    public static final int NUMBER_OF_PARAMETERS = 2;

    public static void main(String[] args) {

        Validator parameterParser = new Validator();
        Writer writer = new Writer();
        
        String dataFile = "";
        String parametersFile = "";
        
        if (args.length < NUMBER_OF_PARAMETERS) {
            System.out.println("Please inform the parameters");
            return;
        }

        dataFile = args[0];
        parametersFile = args[1];

       
        if (Validator.validatePaths(parametersFile, dataFile)) {
            List<Parameters> fieldParams = Parser.parsePolicyFile(parametersFile);
            List<Document> documentList = Parser.parseDataFile(dataFile);

            AnonymizationService anonymization = new AnonymizationService();
            documentList = anonymization.anonymizeData(documentList, fieldParams);

            int indexJson = dataFile.indexOf(".json");
            String path = dataFile.substring(0, indexJson) + "_anonymized.json";
            writer.createAnonymizedDocument(documentList, path);
        }
    }
}
