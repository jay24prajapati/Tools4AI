package com.t4a.predict;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.t4a.action.http.HttpMethod;
import com.t4a.action.http.HttpPredictedAction;
import com.t4a.action.http.InputParameter;
import com.t4a.api.AIAction;
import lombok.extern.java.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * The HttpRestPredictionLoader class is responsible for loading and parsing HTTP REST predictions.
 * It reads the configuration from a JSON file and creates a map of AIAction predictions.
 * The class uses Gson for JSON processing and handles exceptions by throwing a LoaderException.
 * It also constructs a list of action names.
 */
@Log
public class HttpRestPredictionLoader {
    private  String yamlFile = "http_actions.json";
    private URL resourceUrl = null;
    public void load(Map<String, AIAction> predictions, StringBuilder actionNameList) throws LoaderException {

        try {
            parseConfig(predictions,actionNameList);
        } catch (IOException e) {
            throw new LoaderException(e);
        }


    }
    public  void parseConfig(Map<String,AIAction> predictions, StringBuilder actionNameList) throws IOException {
        if(resourceUrl == null)
            resourceUrl = HttpRestPredictionLoader.class.getClassLoader().getResource(yamlFile);

        if (resourceUrl == null) {
            log.warning("File not found: " + yamlFile);
            return;
        }
        Gson gson = new Gson();


        try (InputStream inputStream = resourceUrl.openStream();
             InputStreamReader reader = new InputStreamReader(inputStream)){
            JsonObject rootObject = gson.fromJson(reader, JsonObject.class);
            JsonArray endpoints = rootObject.getAsJsonArray("endpoints");

            for (JsonElement obj : endpoints) {
                JsonObject endpoint = obj.getAsJsonObject();
                String actionName = endpoint.get("actionName").getAsString();
                String url = endpoint.get("url").getAsString();
                String type = endpoint.get("type").getAsString();
                String description = endpoint.get("description").getAsString();
                List<InputParameter> inputObjects = new ArrayList<>();
                JsonArray inputArray = endpoint.getAsJsonArray("input_object");
                for (JsonElement inputElement : inputArray) {
                    JsonObject inputObj = inputElement.getAsJsonObject();
                    String inputname = inputObj.get("name").getAsString();
                    String inputType = inputObj.get("type").getAsString();
                    String inputDescription = inputObj.get("description").getAsString();
                    Object defaultValue = inputObj.get("defaultValue");
                    InputParameter parameter =  new InputParameter(inputname, inputType, inputDescription);
                    if(defaultValue != null) {
                        parameter.setDefaultValue((String)defaultValue);
                    }
                    inputObjects.add(parameter);
                }
                JsonObject outputObject = endpoint.getAsJsonObject("output_object");
                JsonObject authInterface = endpoint.getAsJsonObject("auth_interface");


                HttpPredictedAction httpPredictedAction = new HttpPredictedAction();
                httpPredictedAction.setDescription(description);
                httpPredictedAction.setUrl(url);
                httpPredictedAction.setType(HttpMethod.valueOf(type));
                httpPredictedAction.setAuthInterface(authInterface);
                httpPredictedAction.setInputObjects(inputObjects);
                httpPredictedAction.setOutputObject(outputObject);
                actionNameList.append(actionName);
                actionNameList.append(", ");
                predictions.put(actionName,httpPredictedAction);

            }


        }
    }


}
