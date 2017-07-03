/**
 *
 *
 *
 *
 *
 *
 *
 *
 */
package br.unicamp.ft.priva.utils;

import br.unicamp.ft.priva.models.Document;
import br.unicamp.ft.priva.models.Field;
import br.unicamp.ft.priva.models.Parameters;
import java.io.FileReader;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Parser {

    private static final Logger LOGGER = LoggerFactory.getLogger("PRIVA");

    public static final String FIELD_NAME = "FIELD_NAME";
    public static final String TYPE = "TYPE";
    public static final String DETAIL = "DETAIL";
    public static final String URL_ENCODED_SEPARATOR = "]&[";

    public static List<Parameters> parsePolicyFile(String filePath) {
        JSONParser parser = new JSONParser();
        List<Parameters> listParameters = new ArrayList<>();
        try {
            FileReader fr = new FileReader(filePath);
            Object obj = parser.parse(fr);
            JSONArray jsonArray = (JSONArray) obj;

            return parsePolicy(jsonArray);
        } catch (IOException | ParseException e) {
            LOGGER.error("It's possible to read the parameter file: ", e);
        }
        return listParameters;
    }

    public static List<Parameters> parsePolicy(JSONArray jsonPolicy) {
        JSONObject jsonObject;
        List<Parameters> listParameters = new ArrayList<>();
        for (int i = 0; i < jsonPolicy.size(); i++) {
            jsonObject = (JSONObject) jsonPolicy.get(i);
            Parameters param = new Parameters();
            param.setFieldName((String) jsonObject.get(FIELD_NAME));
            param.setAnonymizationType((String) jsonObject.get(TYPE));
            param.setDetails((String) jsonObject.get(DETAIL));
            listParameters.add(param);
        }
        return listParameters;
    }

    public static List<Parameters> parsePolicy(String jsonPolicy) {
        JSONParser parser = new JSONParser();
        Object obj;
        try {
            obj = parser.parse(jsonPolicy);
            return parsePolicy((JSONArray) obj);
        } catch (ParseException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Parse the data file
     *
     *
     *
     * @param json
     * @return
     */
    public static List<Document> parseData(String json) {
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(json);
            JSONArray jsonArray = (JSONArray) obj;
            return parseData(jsonArray);
        } catch (ParseException e) {
            LOGGER.error("It's possible to read the data file: ", e);
            return null;
        }
    }

    public static List<Document> parseData(JSONArray jsonArray) {
        JSONObject jsonObject;
        List<Field> fieldList;
        List<Document> documentList = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            Document document = new Document();
            jsonObject = (JSONObject) jsonArray.get(i);
            fieldList = new ArrayList<>();
            for (Object key : jsonObject.keySet()) {
                //based on you key types
                String keyStr = (String) key;
                Field field = new Field();
                field.setName(keyStr);
                field.setValue(jsonObject.get(keyStr).toString());
                field.setAnonymization(null);
                fieldList.add(field);
            }
            document.setFields(fieldList);
            documentList.add(document);
        }
        return documentList;
    }

    /**
     * Parse the data file
     *
     * @param filePath
     * @return
     */
    public static List<Document> parseDataFile(String filePath) {
        JSONParser parser = new JSONParser();
        try {
            FileReader fr = new FileReader(filePath);
            Object obj = parser.parse(fr);
            JSONArray jsonArray = (JSONArray) obj;
            return parseData(jsonArray);
        } catch (IOException | ParseException e) {
            LOGGER.error("It's possible to read the data file: ", e);
            return null;
        }
    }
}
