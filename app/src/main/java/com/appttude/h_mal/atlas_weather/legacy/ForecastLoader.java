package com.appttude.h_mal.atlas_weather.legacy;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import androidx.loader.content.AsyncTaskLoader;

import com.appttude.h_mal.atlas_weather.legacy.model.ForecastItem;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.appttude.h_mal.atlas_weather.legacy.data.network.RetrieveJSON.createUrl;
import static com.appttude.h_mal.atlas_weather.legacy.data.network.RetrieveJSON.extractFeatureFromJson;
import static com.appttude.h_mal.atlas_weather.legacy.data.network.RetrieveJSON.makeHttpRequest;

public class ForecastLoader extends AsyncTaskLoader<List<ForecastItem>> {

    private ArrayList<String> mUrl;

    public ForecastLoader(Context context, ArrayList<String> url) {
            super(context);
            this.mUrl = url;
        }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<ForecastItem> loadInBackground() {
        if (mUrl == null) {
            Log.i("", "loadInBackground: " + "null");
            return null;
        }

        String json = null;
        List<ForecastItem> forecastItems = new ArrayList<ForecastItem>();

        for (int i = 0; i < mUrl.size(); i++) {
            try {
                URL url = createUrl(mUrl.get(i));
                json = makeHttpRequest(url);

            } catch (IOException e) {
                Log.e("", "Problem making the HTTP request.", e);
            }finally {
                if (!TextUtils.isEmpty(json)) {
                    forecastItems.add(extractFeatureFromJson(json));
                }
            }
        }

        if (forecastItems.size() < 1){
            return null;
        }

        return forecastItems;
    }

}
