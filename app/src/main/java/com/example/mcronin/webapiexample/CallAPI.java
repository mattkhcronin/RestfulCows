package com.example.mcronin.webapiexample;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by mcronin on 1/22/2016.
 */
public class CallAPI extends AsyncTask<APIRequest, String, APIResponse> {


    private String basePath = "http://restfulapp.azurewebsites.net/api/cow";
    private PostExecutable postExecutable;

    public CallAPI() {

    }

    public CallAPI(PostExecutable postExecutable) {
        this.postExecutable = postExecutable;
    }

    @Override
    protected APIResponse doInBackground(APIRequest... requests) {
        APIResponse response = new APIResponse();
        APIRequest request = requests[0];

        switch (request.getRequestMethod()){
            case GET:
                response = HTTPGet(request);
                break;
            case POST:
                response = HTTPPost(request);
                break;
            case DELETE:
                response = HTTPDelete(request);
                break;
        }
        return response;
    }

    private APIResponse HTTPGet(APIRequest request) {
        APIResponse response = new APIResponse();
        HttpURLConnection urlConnection;
        URL url;
        String path = basePath;
        if (request.getCow() != null && request.getCow().getId() > 0) {
            path = path + "/" + request.getCow().getId();
        }
        try {
            url = new URL(path);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(request.getRequestMethod().toString());
            urlConnection.connect();

            response.setResponseCode(urlConnection.getResponseCode());
            response.setResponseMessage(urlConnection.getResponseMessage());

            InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
            response.setResultJSON(readStream(inputStream));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    private APIResponse HTTPPost(APIRequest request) {
        APIResponse response = new APIResponse();
        HttpURLConnection urlConnection;
        URL url;
        String content = request.getCow().toJSON();
        try {
            url = new URL(basePath);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestMethod(request.getRequestMethod().toString());
            OutputStreamWriter outputStream = new OutputStreamWriter(urlConnection.getOutputStream());
            outputStream.write(content);
            outputStream.flush();
            response.setResponseCode(urlConnection.getResponseCode());
            response.setResponseMessage(urlConnection.getResponseMessage());
            outputStream.close();
            InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
            response.setResultJSON(readStream(inputStream));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    private APIResponse HTTPDelete(APIRequest request){
        APIResponse response = new APIResponse();
        HttpURLConnection urlConnection;
        URL url;
        String path = basePath;
        try{
            path = path + "/" + request.getCow().getId();
            url = new URL(path);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(request.getRequestMethod().toString());
            urlConnection.connect();

            response.setResponseCode(urlConnection.getResponseCode());
            response.setResponseMessage(urlConnection.getResponseMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    private String readStream(InputStream inputStream) {
        String resultJson = "";
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            resultJson = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultJson;
    }


    @Override
    protected void onPostExecute(APIResponse apiResponse) {
        if (postExecutable != null) {
            postExecutable.onPostExecute(apiResponse);
        }
    }

    public enum RequestMethod {
        GET,
        POST,
        DELETE
    }

    public interface PostExecutable {
        void onPostExecute(APIResponse apiResponse);
    }
}
