package com.example.helper;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class CustomVision {

    final static String trainingEndpoint = "eastus.api.cognitive.microsoft.com/";
    final static String trainingApiKey = "0c33e6760a774a86a585b424e6d67722";
    static RestTemplate restTemplate = new RestTemplate();
    final static String projectID = "16f8c3cb-b085-4c6d-9bae-c2844b0ae2e3";
    public final static String tagEric = "9c9a7527-f3a7-4647-a626-5ac238bca547";
    public final static String tagOthers = "70da1954-289d-4e1a-99fa-c85608f90bf1";

    public static void createproject(String projectName) throws JSONException{
        String url = "https://{endpoint}/customvision/v3.3/Training/projects?name={name}";

        Map<String, String> params = new HashMap<>();
        params.put("endpoint", trainingEndpoint);
        params.put("name", projectName);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        // System.out.println(builder.buildAndExpand(params).toUri());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Training-key", trainingApiKey);

        HttpEntity<String> request = new HttpEntity<String>(headers);

        ResponseEntity<String> response = restTemplate.postForEntity(builder.buildAndExpand(params).toUri(), request, String.class);
        // System.out.println(response.getBody())

        JSONObject jsonObject = new JSONObject(response.getBody());
        System.out.println(jsonObject.getString("id"));    
    }

    public static void main(String[] args) throws JSONException, IOException {
        //createproject("lab");
        //createTag("Eric");
        //createTag("Others");
        String fileName = "/Users/tianyuhan/Desktop/face recognition/backend/images/5359c897-3e12-4d8e-bf48-7c85bfb6ffe7.png";
        uploadImage(tagEric, fileName);
    }

    public static void createTag(String tagName) throws JSONException{
        String url = "https://{endpoint}/customvision/v3.3/Training/projects/{projectId}/tags?name={name}";

        Map<String, String> params = new HashMap<>();
        params.put("endpoint", trainingEndpoint);
        params.put("projectId", projectID);
        params.put("name", tagName);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        URI uri = builder.buildAndExpand(params).toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Training-key", trainingApiKey);

        HttpEntity<String> request = new HttpEntity<String>(headers);

        ResponseEntity<String> response = restTemplate.postForEntity(uri, request, String.class);
        // System.out.println(response.getBody());

        JSONObject jsonObject = new JSONObject(response.getBody());
        System.out.println(jsonObject.getString("id") + ": " + tagName);
    }

    public static void uploadImage(String tagId, String fileName) throws JSONException, IOException {
        String url = "https://{endpoint}/customvision/v3.3/training/projects/{projectId}/images?tagIds={tagIds}";

        Map<String, String> params = new HashMap<>();
        params.put("endpoint", trainingEndpoint);
        params.put("projectId", projectID);
        params.put("tagIds", tagId);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        URI uri = builder.buildAndExpand(params).toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.set("Training-key", trainingApiKey);

        Path path = Paths.get(fileName);
        byte[] imageFile = Files.readAllBytes(path);

        HttpEntity<byte[]> request = new HttpEntity<>(imageFile, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(uri, request, String.class);
        System.out.println(response.getBody());
}

public static ResponseEntity<String> validate(byte[] data) throws IOException {
    
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/octet-stream");
    headers.add("Prediction-Key", "0c33e6760a774a86a585b424e6d67722");

    HttpEntity<byte[]> entity = new HttpEntity<>(data, headers);
    String URL = "https://eastus.api.cognitive.microsoft.com/customvision/v3.0/Prediction/16f8c3cb-b085-4c6d-9bae-c2844b0ae2e3/classify/iterations/Iteration2/image";
    ResponseEntity<String> result = restTemplate.postForEntity(URL, entity, String.class);
    return result;
}
}
