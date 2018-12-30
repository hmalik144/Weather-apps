package com.appttude.h_mal.atlas_weather;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.appttude.h_mal.atlas_weather.dbfiles.ForecastContract.ForecastEntry;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.appttude.h_mal.atlas_weather.RetrieveJSON.UriBuilder;
import static com.appttude.h_mal.atlas_weather.RetrieveJSON.createUrl;

public class AddForecast extends AppCompatActivity {

    private EditText tv;
    private String nameString;
    public ProgressBar progBarAdd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_forecast);

        tv = findViewById(R.id.location_name_tv);
        progBarAdd = findViewById(R.id.pb_add);
        progBarAdd.setVisibility(View.GONE);

        Button button = findViewById(R.id.submit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameString = tv.getText().toString().trim();
                submitEntry(nameString);
            }
        });

    }

    private void submitEntry(String s){

        if (TextUtils.isEmpty(s)) {
            Toast.makeText(AddForecast.this, "please insert a location name", Toast.LENGTH_SHORT).show();
            return;
        }

        new RetrieveFeedTask().execute(s);

    }

    private void insertIntoDb(){

        ContentValues values = new ContentValues();
        values.put(ForecastEntry.COLUMN_FORECAST_NAME, nameString);

            Uri newUri = getContentResolver().insert(ForecastEntry.CONTENT_URI, values);

            if (newUri == null) {
                Toast.makeText(this, getString(R.string.insert_entry_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.insert_entry_successful),
                        Toast.LENGTH_SHORT).show();
            }

    }

    public class RetrieveFeedTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progBarAdd.setVisibility(View.VISIBLE);
        }

        protected Boolean doInBackground(String... urls) {
            HttpURLConnection urlConnection = null;
            URL url = createUrl(UriBuilder(urls[0]));

            Boolean b = null;

            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(30000);
                urlConnection.setConnectTimeout(30000);
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                if (urlConnection.getResponseCode() == 200) {
                    b = true;
                    Log.i("", "checkHttpRequest: good response");
                } else {
                    b = false;
                    Log.e("", "Error response code: " + urlConnection.getResponseCode());
                }
            } catch (IOException e) {
                Log.e("", "Problem retrieving the response results.", e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }

            return b;
        }

        protected void onPostExecute(Boolean feed) {

            progBarAdd.setVisibility(View.GONE);

            if (feed == null){
                Toast.makeText(AddForecast.this, "No connection", Toast.LENGTH_SHORT).show();
                return;
            }

            if (feed){

                insertIntoDb();
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", getClass().getSimpleName());
                setResult(RESULT_OK, returnIntent);
                finish();
            }else{
                Toast.makeText(AddForecast.this, "Location not found", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
