package com.example.mcronin.webapiexample;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<Cow> cows;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        cows = new ArrayList<>();
        RefreshList();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddCow();
            }
        });
    }

    private void AddCow(){
        CallAPI callAPI = new CallAPI(new CallAPI.PostExecutable() {
            @Override
            public void onPostExecute(APIResponse apiResponse) {
                RefreshList();
            }
        });
        APIRequest request = new APIRequest(CallAPI.RequestMethod.POST, new Cow(7, "New Cow", "New Color"));
        callAPI.execute(request);
    }

    private void RefreshList(){
        CallAPI callAPI = new CallAPI(new CallAPI.PostExecutable() {
            @Override
            public void onPostExecute(APIResponse apiResponse) {
                SetCowList(apiResponse);
            }
        });
        APIRequest request = new APIRequest(CallAPI.RequestMethod.GET, null);
        callAPI.execute(request);
    }

    private void SetCowList(APIResponse apiResponse){
        if (apiResponse.getResultJSON() != null) {
            try {
                cows.clear();
                JSONArray jsonArray = new JSONArray(apiResponse.getResultJSON());
                for (int i = 0; jsonArray.length() > i; i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    cows.add(new Cow(jsonObject.getInt("Id"), jsonObject.getString("Breed"), jsonObject.getString("Color")));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ListView cowList = (ListView) findViewById(R.id.cowList);
            ArrayAdapter<Cow> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cows);
            cowList.setAdapter(adapter);
        }
    }
}
