package br.com.tuxknife;

public class CommandResponse {
    private String response;
    private String error;

    public CommandResponse(String response, String error) {
        this.response = response;
        this.error = error;
    }

    public String getResponse() {
        return response;
    }

    public String getError() {
        return error;
    }

}
