package com.appttude.h_mal.atlas_weather;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.util.List;

import static com.appttude.h_mal.atlas_weather.RetrieveJSON.UriBuilder;

public class WorldItemActivity extends AppCompatActivity
        implements android.support.v4.app.LoaderManager.LoaderCallbacks<List<ForecastItem>> {

    private static final int NEWS_LOADER_ID = 1;

    RecyclerView forecastsListView;
    LinearLayout emptyList;
    ProgressBar mainPG;
    SwipeRefreshLayout mSwipeRefreshLayout;

    private RecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);

        Intent mIntent  = getIntent();
        ForecastItem forecast  = mIntent.getParcelableExtra("ForecastItem");

        forecastsListView = findViewById(R.id.forecast_listview);
        forecastsListView.setLayoutManager(new LinearLayoutManager(this));
        emptyList = findViewById(R.id.emptyView);
        mainPG = findViewById(R.id.mainPB);

        emptyList.setVisibility(View.GONE);
        mainPG.setVisibility(View.GONE);

        mAdapter = new RecyclerViewAdapter(this,forecast);
        forecastsListView.setAdapter(mAdapter);

        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setRefreshing(false);

//        if (networkInfo != null && networkInfo.isConnected()) {
//            android.support.v4.app.LoaderManager loaderManager = getSupportLoaderManager();
//            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
//        } else {
//            emptyList.setVisibility(View.VISIBLE);
//            mainPG.setVisibility(View.GONE);
//        }
    }

    @Override
    public android.support.v4.content.Loader<List<ForecastItem>> onCreateLoader(int id, Bundle args) {
//        URL url = createUrl(UriBuilder());
//        ArrayList<String> entries = new ArrayList<>();
//        entries.add(url.toString());
//        return new ForecastLoader(this,entries);
        return null;
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<List<ForecastItem>> loader, List<ForecastItem> data) {

    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<List<ForecastItem>> loader) {
//        loader.reset();
    }
}
