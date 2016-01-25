package com.example.mcronin.webapiexample;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

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
                showAddDialog();
            }
        });
    }

    private void AddCow(int id, String breed, String color){
        CallAPI callAPI = new CallAPI(new CallAPI.PostExecutable() {
            @Override
            public void onPostExecute(APIResponse apiResponse) {
                RefreshList();
            }
        });
        APIRequest request = new APIRequest(CallAPI.RequestMethod.POST, new Cow(id, breed, color));
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

    private void DeleteCow(int id){
        CallAPI callAPI = new CallAPI(new CallAPI.PostExecutable() {
            @Override
            public void onPostExecute(APIResponse apiResponse) {
                RefreshList();
            }
        });
        APIRequest request = new APIRequest(CallAPI.RequestMethod.DELETE, new Cow(id, "",""));
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
            cowList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    showDeleteDialog(cows.get(position).getId());
                }
            });
        }
    }

    private void showDeleteDialog(final int id){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_alert)
                .setPositiveButton(R.string.yes_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DeleteCow(id);
                    }
                })
                .setNegativeButton(R.string.no_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // cancel the action
                    }
                });
         AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showAddDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.add_title);

        LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.add_cow_dialog, null);
        builder.setView(view);

        builder.setMessage(R.string.delete_alert)
                .setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText cow_id = (EditText) view.findViewById(R.id.cow_id);
                        if (!cow_id.getText().toString().equals("")) {
                            int id = Integer.parseInt(cow_id.getText().toString());
                            
                            EditText cow_breed = (EditText) view.findViewById(R.id.cow_breed);
                            String breed = cow_breed.getText().toString();
                            
                            EditText cow_color = (EditText) view.findViewById(R.id.cow_color);
                            String color = cow_color.getText().toString();
                            
                            AddCow(id, breed, color);
                        } else {
                            Toast.makeText(MainActivity.this, R.string.invalid_toast, Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // cancel the action
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
