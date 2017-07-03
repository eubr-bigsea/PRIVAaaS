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
package br.unicamp.ft.priva.aas;

import br.unicamp.ft.priva.models.Document;
import br.unicamp.ft.priva.models.Field;
import br.unicamp.ft.priva.models.Parameters;
import br.unicamp.ft.priva.service.AnonymizationService;
import br.unicamp.ft.priva.utils.Parser;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.Part;
import javax.ws.rs.core.MediaType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.staticFiles;
import spark.utils.IOUtils;

/**
 *
 * REST API for PRIVAaaS
 *
 *
 *
 * @author nmsa
 */
public class LightPrivacyService {

    private static final Logger LOGGER = LoggerFactory.getLogger("PRIVAaaS");

    public static void main(String[] args) throws UnknownHostException {
        staticFiles.location("/public");

        // Main handle
        post("/endpoint", (req, res) -> handlePost(req, res));
        post("/endpoint/", (req, res) -> handlePost(req, res));

        // Default info handle
        get("*", (req, res) -> IOUtils.toString(spark.Spark.class.getResourceAsStream("/public/index.html")));

        // Ready info
        spark.Spark.awaitInitialization();

        LOGGER.info("\n\nLightPrivacyService v0.1 is listening at http://0.0.0.0:{}/priva/ \n\n", spark.Spark.port());
    }

    private static Object handlePost(Request req, Response res) {
        final String contentType = req.contentType();
        LOGGER.trace("post {} ", contentType);
        if (contentType == null) {
            return "Invalid Content Type";
        }
        if (contentType.startsWith(MediaType.APPLICATION_JSON)) {
            LOGGER.info("post {} ", MediaType.APPLICATION_JSON);
            return handleSingleJSON(req);
        }
        if (contentType.startsWith(MediaType.APPLICATION_FORM_URLENCODED)) {
            LOGGER.info("post {} ", MediaType.APPLICATION_FORM_URLENCODED);
            return handleURLEncoded(req);
        }
        if (contentType.startsWith(MediaType.MULTIPART_FORM_DATA)) {
            LOGGER.info("post {} ", MediaType.MULTIPART_FORM_DATA);
            return handleMultipart(req);
        }
        String error = MessageFormat.format("Invalid Content Type! Send: {0} or {1} or {2}", MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA);
        LOGGER.info(error);
        return error;
    }

    private static Object handleSingleJSON(Request req) {
//        if (!MediaType.APPLICATION_JSON.equals(req.contentType())) {
//            return "Wrong format. Send " + MediaType.APPLICATION_JSON;
//        }
        JSONParser jp = new JSONParser();
        JSONArray input;
        try {
            input = (JSONArray) jp.parse(req.body());
        } catch (ParseException ex) {
            LOGGER.error("Wrong data. Send correct: ", ex);
            return "Wrong data. Send correct: " + MediaType.APPLICATION_JSON;
        }

        JSONArray policy = (JSONArray) input.remove(0);
        List<Parameters> parameters = Parser.parsePolicy(policy);
        List<Document> documentList = new ArrayList<>();
        input.forEach((object) -> {
            documentList.addAll(Parser.parseData((JSONArray) object));
        });
        return anonymizationProcess(documentList, parameters);
    }

    private static Object handleURLEncoded(Request req) {
//        if (!MediaType.APPLICATION_FORM_URLENCODED.equals(req.contentType())) {
//            return "Wrong format. Send " + MediaType.APPLICATION_FORM_URLENCODED;
//        }
        String content = req.body();
        int indexOf = content.indexOf(Parser.URL_ENCODED_SEPARATOR);
        if (indexOf < 0) {
            return "Wrong format. Send [Policy]&[Data...]";
        }
        String policy = content.substring(0, indexOf + 1);
        content = content.substring(indexOf + 2);
        List<String> data = new ArrayList<>();
        while ((indexOf = content.indexOf(Parser.URL_ENCODED_SEPARATOR)) > 0) {
            data.add(content.substring(0, indexOf + 1));
            content = content.substring(indexOf + 2);
        }
        data.add(content);
        return handleSplitContent(policy, data);
    }

    private static Object handleMultipart(Request req) {
//        @TODO: when spark fixes bug, correct.
        MultipartConfigElement multipartConfigElement = new MultipartConfigElement(System.getProperty("java.io.tmpdir"));
        req.raw().setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement);

        List<String> data = getDataFromParts(req);
        if (data == null) {
            // workaround for the systems that do not support parts
            LOGGER.debug("Wrong parts, using workaround.");
            data = parseContentWorkaround(req);
        }

        String policy = data.remove(0);

        return handleSplitContent(policy, data);
    }

    private static String handleSplitContent(final String policyJSON, final List<String> dataJSON) {
        JSONParser jp = new JSONParser();
        JSONArray policy;
        try {
            policy = (JSONArray) jp.parse(policyJSON);
        } catch (ParseException ex) {
            LOGGER.error("Wrong data format. Send correct format: ", ex);
            return "Wrong data. Send correct: " + MediaType.APPLICATION_JSON;
        }
        List<Parameters> parameters = Parser.parsePolicy(policy);
        List<Document> documentList = new ArrayList<>();
        dataJSON.forEach((datum) -> {
            try {
                documentList.addAll(Parser.parseData((JSONArray) jp.parse(datum)));
            } catch (ParseException ex) {
                LOGGER.error("Wrong data. Send correct: ", ex);
            }
        });
        return anonymizationProcess(documentList, parameters);
    }

    private static String anonymizationProcess(List<Document> documentList, List<Parameters> parameters) {
        AnonymizationService anonymization = new AnonymizationService();
        documentList = anonymization.anonymizeData(documentList, parameters);
        JSONArray jsonArray = new JSONArray();
        for (Document document : documentList) {
            List<Field> fields = document.getFields();
            JSONObject jsonObject = new JSONObject();
            fields.forEach((field) -> {
                jsonObject.put(field.getName(), field.getValue());
            });
            jsonArray.add(jsonObject);
        }
        return jsonArray.toJSONString();
    }

    private static List<String> getDataFromParts(Request req) {
        int read;
        char buffer[] = new char[1024 * 1024];
        List<String> data = new ArrayList<>();
        try {
            Collection<Part> parts = req.raw().getParts();
            for (Part part : parts) {
                if (part.getContentType().startsWith(MediaType.APPLICATION_JSON)) {
                    InputStreamReader isr = new InputStreamReader(part.getInputStream());
                    StringBuilder sb = new StringBuilder();
                    while ((read = isr.read(buffer)) > 0) {
                        sb.append(buffer, 0, read);
                    }
                    data.add(sb.toString());
                } else {
                    LOGGER.warn("Wrong format <{}> in file <{}>.", part.getContentType(), part.getName());
                }
            }
        } catch (IOException | ServletException ex) {
            LOGGER.trace("No parts supported, using workaround!");
            return null;
        }
        return data;
    }

    private static List<String> parseContentWorkaround(Request req) {
        String contentType = req.contentType();
        String body = req.body();

        String boundary0 = contentType.substring(contentType.indexOf("boundary=") + "boundary=".length());
        String boundary1 = body.substring(0, body.indexOf("\n"));
        if (boundary1.contains(boundary0)) {
            LOGGER.info("Boundary found!");
        } else {
            LOGGER.error("Boundary inconcistency ({}) != ({})!", boundary0, boundary1);
        }

        List<String> data = new ArrayList<>();
        String[] split = body.split(boundary0);
        for (String part : split) {
            final int init = part.indexOf('[');
            final int end = part.lastIndexOf(']') + 1;
            if (init < 0 || end < 0) {
                LOGGER.error("Invalid content:<{}>", part);
            } else {
                if (part.contains("policyfile")) {
                    data.add(0, part.substring(init, end));
                } else {
                    data.add(part.substring(init, end));
                }
            }
        }
        return data;
    }
}
