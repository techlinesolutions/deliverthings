package com.techlinemobile.mapapplication;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.support.v7.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> imageUrl = new ArrayList<>();
    ArrayList<String> desc = new ArrayList<>();
    ArrayList<String> id = new ArrayList<>();
    ArrayList<String> address = new ArrayList<>();
    ArrayList<String> lat = new ArrayList<>();
    ArrayList<String> lng = new ArrayList<>();
    private static final String TAG = "MAIN";
    private Bundle extras;
    private String globalSearchResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "before loadResultView");
        makeFetchJSonQuery();
        Log.d(TAG, "after loadResultView");
    }

    private void makeFetchJSonQuery() {
        final URL fetchJSonData = NetworkUtils.buildCountryListUrl();
        Log.d(TAG, "fetchJSonData is: " + fetchJSonData.toString());
        new Thread(new Runnable() {
            public void run() {
                new jsonUrlQueryTask().execute(fetchJSonData);
            }
        }).start();
    }
    
    public class jsonUrlQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String jsonSearchResults = null;
            try {
                jsonSearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
                Log.d(TAG, "jsonSearchResults is : " + jsonSearchResults);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return jsonSearchResults;
        }

        @Override
        protected void onPostExecute(String jsonSearchResults) {
            if (jsonSearchResults != null && !jsonSearchResults.equals("")) {
                Log.d(TAG, "jsonSearchResults is :" + jsonSearchResults);
                globalSearchResult = jsonSearchResults;
                loadResultView();
            }
        }
    }

    private String globalFetchResultMethod() {
        return globalSearchResult;
    }

    private void loadResultView() {

            // get the reference of RecyclerView
            RecyclerView recyclerView = findViewById(R.id.list);
            // set a LinearLayoutManager with default vertical orientation
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(linearLayoutManager);
        String strResult = globalFetchResultMethod();
        strResult= " {\"Deliveries\": " + strResult + "}";

        // get JSONObject from JSON file
        JSONObject obj = null;
        try {
            obj = new JSONObject(strResult);

            // fetch JSONArray named Countrys
            JSONArray DeliveriesArray = obj.getJSONArray("Deliveries");
            for (int i = 0; i < DeliveriesArray.length(); i++) {
                JSONObject srDetail = DeliveriesArray.getJSONObject(i);
                desc.add(srDetail.getString("description"));
                Log.d(TAG, "desc is: " + desc);
                id.add(srDetail.getString("id"));
                Log.d(TAG, "id is: " + id);
                imageUrl.add(srDetail.getString("imageUrl"));
                Log.d(TAG, "imageUrl is: " + imageUrl);
                String strLocation = srDetail.getString("location");
                strLocation = strLocation.replace("{", "[{");
                strLocation = strLocation.replace("}", "}]");

                JSONArray jAr = new JSONArray(strLocation);
                lat.add( jAr.getJSONObject(0).getString("lat"));
                Log.d(TAG, "lat is: " + lat);
                lng.add(jAr.getJSONObject(0).getString("lng"));
                Log.d(TAG, "lng is: " + lng);
                address.add(jAr.getJSONObject(0).getString("address"));
                Log.d(TAG, "address is: " + address);



            }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //  call the constructor of CustomAdapter to send the reference and data to Adapter
            CustomAdapter customAdapter = new CustomAdapter(MainActivity.this, imageUrl,
                    desc,
                    id,
                    address,
                    lng,
                    lat);
            recyclerView.setAdapter(customAdapter); // set the Adapter to RecyclerView

    }
}
