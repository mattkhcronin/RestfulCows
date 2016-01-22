package com.example.mcronin.webapiexample;

/**
 * Created by mcronin on 1/22/2016.
 */
public class APIResponse {

    public APIResponse(){

    }

    public APIResponse(int responseCode, String responseMessage, String resultJSON) {
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
        this.resultJSON = resultJSON;
    }

    private int responseCode;
    private String responseMessage;
    private String resultJSON;

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getResultJSON() {
        return resultJSON;
    }

    public void setResultJSON(String resultJSON) {
        this.resultJSON = resultJSON;
    }
}
