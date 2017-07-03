/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.unicamp.ft.priva.aas.client;

import java.io.FileReader;
import java.io.IOException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author nmsa
 */
public class RESTUsageExample {

    private static final String ENDPOINT = "http://localhost:4567/endpoint/";

    public static void main(String[] args) {
        Object policyJSON, dataJSON;
        //Example Data:
        String policyFile = "../priva-poc/input/example-mock-data/mock_data.policy.json";
        String dataFile = "../priva-poc/input/example-mock-data/mock_data.json";

        // Load Example Data:
        JSONParser parser = new JSONParser();
        try {

            FileReader policyReader = new FileReader(policyFile);
            policyJSON = parser.parse(policyReader);

            FileReader dataReader = new FileReader(dataFile);
            dataJSON = parser.parse(dataReader);
        } catch (IOException | ParseException e) {
            System.out.println("IOException | ParseExceptionƒ = " + e);
            return;
        }

        System.out.println("policyJSON = " + policyJSON.toString().length());
        System.out.println("dataJSON   = " + dataJSON.toString().length());

        JSONArray payload = new JSONArray();
        payload.add(policyJSON);
        payload.add(dataJSON);

        // POST data to server
        Client client = ClientBuilder.newClient();

        WebTarget statistics = client.target(ENDPOINT);
        String content = statistics.request(MediaType.APPLICATION_JSON).post(Entity.entity(payload, MediaType.APPLICATION_JSON), String.class);

        System.out.println("result = " + content);

    }
}
