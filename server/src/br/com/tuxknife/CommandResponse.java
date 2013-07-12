package br.com.tuxknife;

public class CommandResponse {
    private String resource;
    private String error;

    public CommandResponse(String resource, String error) {
        this.resource = resource;
        this.error = error;
    }

    public String getResource() {
        return resource;
    }

    public String getError() {
        return error;
    }

}
