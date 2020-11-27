package com.appttude.h_mal.atlas_weather.legacy.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import com.appttude.h_mal.atlas_weather.R;
import com.appttude.h_mal.atlas_weather.legacy.ForecastLoader;
import com.appttude.h_mal.atlas_weather.legacy.data.sql.ForecastContract;
import com.appttude.h_mal.atlas_weather.legacy.data.sql.ForecastContract.ForecastEntry;
import com.appttude.h_mal.atlas_weather.legacy.model.ForecastItem;
import com.appttude.h_mal.atlas_weather.legacy.ui.AddForecastActivity;
import com.appttude.h_mal.atlas_weather.legacy.ui.WorldItemActivity;
import com.appttude.h_mal.atlas_weather.legacy.ui.adapters.CurrentForecastAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import static com.appttude.h_mal.atlas_weather.legacy.data.network.RetrieveJSON.UriBuilder;
import static com.appttude.h_mal.atlas_weather.legacy.ui.home.MainActivity.networkInfo;

public class FragmentTwo extends Fragment implements LoaderManager.LoaderCallbacks<List<ForecastItem>>{

    private static final int NEWS_LOADER_ID = 1;
    private String TAG = getClass().getSimpleName();

    CurrentForecastAdapter mAdapter;
    ProgressBar pb_2;
    LinearLayout emptyView;
    ListView lv;
    LoaderManager loaderManager;

    public FragmentTwo() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment__two, container, false);

        emptyView = rootView.findViewById(R.id.emptyView);
        pb_2 = rootView.findViewById(R.id.progressBar2);

        lv = rootView.findViewById(R.id.listview);

        final LoaderManager.LoaderCallbacks callbacks = this;
        loaderManager = getLoaderManager();
        loaderManager.initLoader(NEWS_LOADER_ID, null, callbacks);

        mAdapter = new CurrentForecastAdapter(getActivity(), new ArrayList<ForecastItem>());
        lv.setAdapter(mAdapter);
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {

                new AlertDialog.Builder(getContext())
                        .setTitle("Delete?")
                        .setMessage("Continue?")
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                Cursor cursor = getActivity().getContentResolver().query(ForecastContract.ForecastEntry.CONTENT_URI,
                                        new String[]{ForecastContract.ForecastEntry._ID,ForecastContract.ForecastEntry.COLUMN_FORECAST_NAME},
                                        null,null,null);

                                cursor.moveToPosition(i);
                                int id = cursor.getInt(cursor.getColumnIndexOrThrow(ForecastContract.ForecastEntry._ID));
                                String selection = ForecastContract.ForecastEntry._ID + "=?";
                                getActivity().getContentResolver().delete(ForecastContract.ForecastEntry.CONTENT_URI,
                                selection, new String[]{id + ""});

                                loaderManager.restartLoader(NEWS_LOADER_ID, null, callbacks);
                            }
                        }).create().show();

                return true;
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), WorldItemActivity.class);
                intent.putExtra("ForecastItem",mAdapter.getItem(i));
                startActivity(intent);
            }
        });

        FloatingActionButton fab = rootView.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), AddForecastActivity.class);
                startActivity(i);
            }
        });

        if (networkInfo != null && networkInfo.isConnected()) {
            loaderManager.initLoader(NEWS_LOADER_ID, null, callbacks);
        } else {
//            emptyList.setVisibility(View.VISIBLE);
//            mainPG.setVisibility(View.GONE);
        }

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        loaderManager.restartLoader(NEWS_LOADER_ID, null, this);
    }

    @Override
    public Loader<List<ForecastItem>> onCreateLoader(int id, Bundle args) {
        pb_2.setVisibility(View.VISIBLE);
        lv.setVisibility(View.GONE);

        ArrayList<String> entries = new ArrayList<>();

        String[] projection = {ForecastEntry.COLUMN_FORECAST_NAME};
        Cursor cursor = getActivity().getContentResolver().query(ForecastEntry.CONTENT_URI,
                projection,null,null,null);

        try {
            while (cursor.moveToNext()) {
                final String descriptionColumnIndex = cursor.getString(cursor.getColumnIndexOrThrow(ForecastEntry.COLUMN_FORECAST_NAME));
                entries.add(UriBuilder(descriptionColumnIndex));
            }
        }catch (Exception e){
            Log.e(TAG, "onCreateLoader: ",e );
        }finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return new ForecastLoader(getContext(),entries);
    }

    @Override
    public void onLoadFinished(Loader<List<ForecastItem>> loader, List<ForecastItem> data) {
        mAdapter.clear();

    }

    @Override
    public void onLoaderReset(Loader<List<ForecastItem>> loader) {
        pb_2.setVisibility(View.GONE);
        mAdapter.clear();
    }

}
