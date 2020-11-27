package com.appttude.h_mal.atlas_weather.legacy.data.network;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.appttude.h_mal.atlas_weather.legacy.model.CurrentForecast;
import com.appttude.h_mal.atlas_weather.legacy.model.Forecast;
import com.appttude.h_mal.atlas_weather.legacy.model.ForecastItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static com.appttude.h_mal.atlas_weather.legacy.services.location.getLatLong.latitude;
import static com.appttude.h_mal.atlas_weather.legacy.services.location.getLatLong.longitude;

/**
 * Created by h_mal on 05/05/2018.
 */

public class RetrieveJSON {

    public RetrieveJSON(){}

    protected static String APIkey = "1fe09c8cd3c42e573c5cc7c32b27a1b4";

    public static String UriBuilder(int days){

        String latLong = latitude + "," + longitude;

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority("api.weatherstack.com")
                .appendPath("current")
                .appendQueryParameter("access_key",APIkey)
                .appendQueryParameter("query",latLong);

        return builder.build().toString().replace("%2C",",");

    }

    public static String UriBuilder(String q){

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority("api.apixu.com")
                .appendPath("v1")
                .appendPath("forecast.json")
                .appendQueryParameter("key",APIkey)
                .appendQueryParameter("q",q)
                .appendQueryParameter("days","7");

        return builder.build().toString().replace("%2C",",");

    }

    public static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e("ERROR", "Error with creating URL ", e);
        }
        return url;
    }

    public static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = null;

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(30000);
            urlConnection.setConnectTimeout(30000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e("", "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e("", "Problem retrieving the JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }

        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = "";
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        Log.d("", output.toString());
        return output.toString();

    }

    public static ForecastItem extractFeatureFromJson(String newsJSON) {

        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        CurrentForecast forecastCurrent = new CurrentForecast();
        List<Forecast> forecasts = new ArrayList<Forecast>();

        try {
            JSONObject baseJsonResponse = new JSONObject(newsJSON);
            JSONObject locationObject = baseJsonResponse.getJSONObject("location");
            Double latitude = locationObject.getDouble("lat");
            Double longitude = locationObject.getDouble("lon");
            String location = locationObject.getString("name");
            int last_updated_epoch = locationObject.getInt("localtime_epoch");

            JSONObject currentObject = baseJsonResponse.getJSONObject("current");
            Double temp_c = currentObject.getDouble("temp_c");
            Double temp_f = currentObject.getDouble("temp_f");

            JSONObject conditionObject = currentObject.getJSONObject("condition");
            String condition_text = conditionObject.getString("text");
            String iconURL = conditionObject.getString("icon");

            Double wind_mph = currentObject.getDouble("wind_mph");
            Double wind_kph = currentObject.getDouble("wind_kph");
            String wind_dir = currentObject.getString("wind_dir");
            Double pressure_mb = currentObject.getDouble("pressure_mb");
            Double pressure_in = currentObject.getDouble("pressure_in");
            Double precip_mm = currentObject.getDouble("precip_mm");
            Double precip_in = currentObject.getDouble("precip_in");
            Double humidity = currentObject.getDouble("humidity");
            Double cloud = currentObject.getDouble("cloud");
            Double feelslike_c = currentObject.getDouble("feelslike_c");
            Double feelslike_f = currentObject.getDouble("feelslike_f");
            Double vis_km = currentObject.getDouble("vis_km");
            Double vis_miles = currentObject.getDouble("vis_miles");

            forecastCurrent = new CurrentForecast(location, latitude, longitude,
                    last_updated_epoch, temp_c,  temp_f,  condition_text,
                    iconURL,  wind_mph,  wind_kph,  wind_dir,
                    pressure_mb,  pressure_in,  precip_mm,  precip_in,
                    humidity,  cloud,  feelslike_c,  feelslike_f,
                    vis_km,  vis_miles
            );

            JSONObject forecastObject = baseJsonResponse.getJSONObject("forecast");
            JSONArray forecastsArray = forecastObject.getJSONArray("forecastday");

            for (int i = 0; i < forecastsArray.length(); i++) {
                JSONObject currentForecastObject = forecastsArray.getJSONObject(i);
                Long date = currentForecastObject.getLong("date_epoch");

                JSONObject day = currentForecastObject.getJSONObject("day");
                Double maxtemp_c = day.getDouble ("maxtemp_c");
                Double maxtemp_f = day.getDouble ("maxtemp_f");
                Double mintemp_c = day.getDouble ("mintemp_c");
                Double mintemp_f = day.getDouble ("mintemp_f");
                Double avgtemp_c = day.getDouble ("avgtemp_c");
                Double avgtemp_f = day.getDouble ("avgtemp_f");
                Double maxwind_mph = day.getDouble ("maxwind_mph");
                Double maxwind_kph = day.getDouble ("maxwind_kph");
                Double totalprecip_mm = day.getDouble ("totalprecip_mm");
                Double totalprecip_in = day.getDouble ("totalprecip_in");
                Double avgvis_km = day.getDouble ("avgvis_km");
                Double avgvis_miles = day.getDouble ("avgvis_miles");
                Double avghumidity = day.getDouble ("avghumidity");
                Double uv = day.getDouble("uv");

                JSONObject condition = day.getJSONObject("condition");
                String conditionText = condition.getString("text");
                String imageURL = condition.getString("icon");

                JSONObject astro = currentForecastObject.getJSONObject("astro");
                String sunrise = astro.getString("sunrise");
                String sunset = astro.getString("sunset");
                String moonrise = astro.getString("moonrise");
                String moonset = astro.getString("moonset");

                Forecast forecast = new Forecast(date,  maxtemp_c,  maxtemp_f,  mintemp_c,  mintemp_f,  avgtemp_c,  avgtemp_f,
                        maxwind_mph,  maxwind_kph,  totalprecip_mm,  totalprecip_in,  avgvis_km,  avgvis_miles,
                        avghumidity,  conditionText,  imageURL,  uv,  sunrise,  sunset,  moonrise,  moonset)
                        ;

                forecasts.add(forecast);

            }

        } catch (JSONException e) {
            Log.e("Error", "Problem parsing the book JSON results", e);
        }

        return new ForecastItem(forecastCurrent,forecasts);
    }
}
