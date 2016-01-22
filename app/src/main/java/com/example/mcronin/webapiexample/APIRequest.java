package com.example.mcronin.webapiexample;

/**
 * Created by mcronin on 1/22/2016.
 */
public class APIRequest {


    public APIRequest(CallAPI.RequestMethod requestMethod, Cow cow) {
        this.requestMethod = requestMethod;
        this.cow = cow;
    }

    private CallAPI.RequestMethod requestMethod;
    private Cow cow;

    public CallAPI.RequestMethod getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(CallAPI.RequestMethod requestMethod) {
        this.requestMethod = requestMethod;
    }

    public Cow getCow() {
        return cow;
    }

    public void setCow(Cow cow) {
        this.cow = cow;
    }
}
