package br.com.tuxknife;

import com.google.gson.JsonObject;

public class CommandResponse {
    private String resource;
    private String responseError;
    private String responseData;
    private transient JsonObject json;

    public CommandResponse() {
        json = new JsonObject();
    }

    public CommandResponse withError(String responseError) {
        this.responseError = responseError;
        return this;
    }

    public CommandResponse withResource(String resource) {
        this.resource = resource;
        return this;
    }

    public void addResponseData(String key, String value) {
        json.addProperty(key, value);
        responseData = json.toString();
    }

    public void setResource(String resource) {
        this.resource = resource;
    }
}
