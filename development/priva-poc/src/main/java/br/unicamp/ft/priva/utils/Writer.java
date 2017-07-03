package br.unicamp.ft.priva.utils;

import br.unicamp.ft.priva.models.Document;
import br.unicamp.ft.priva.models.Field;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Writer {

    /**
     * Create the anonymized json file
     *
     * @param documents
     * @param path
     */
    public void createAnonymizedDocument(List<Document> documents, String path) {

        JSONArray jsonArray = new JSONArray();
        FileWriter writeFile = null;

        for (Document document : documents) {
            List<Field> fields = document.getFields();
            JSONObject jsonObject = new JSONObject();
            for (Field field : fields) {
                jsonObject.put(field.getName(), field.getValue());
            }
            jsonArray.add(jsonObject);
        }

        try {
            writeFile = new FileWriter(path);
            writeFile.write(jsonArray.toJSONString());
            writeFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.print("Anonymized document generated in: " + path);

    }

}
