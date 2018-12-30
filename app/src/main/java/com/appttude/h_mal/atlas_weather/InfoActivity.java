package com.appttude.h_mal.atlas_weather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_dialog_layout);

        List<infopageItem> infopageItemList = new ArrayList<>();
        infopageItemList.add(new infopageItem(R.drawable.day_305,
                "Weather data and icons provided by: ",null,null,
                "https://www.apixu.com/","APIXU"));
        infopageItemList.add(new infopageItem(R.drawable.somethingnew,
                "Icon made by: ",
                "https://www.flaticon.com/authors/hirschwolf",
                "Hirschwolf",
                null,
                null));
        infopageItemList.add(new infopageItem(R.drawable.breeze,
                "Icon made by: ",
                "https://www.flaticon.com/authors/hirschwolf",
                "Hirchwolf",
                null,null));
        infopageItemList.add(new infopageItem(R.drawable.water_drop,
                "Icon made by: ",
                "https://www.flaticon.com/authors/freepik",
                "Freepic",
                null,null));
        infopageItemList.add(new infopageItem(R.drawable.cloud_symbol,
                "Icon made by: ",
                "https://www.flaticon.com/authors/simpleicon",
                "Simple Icon",
                null,null));
        infopageItemList.add(new infopageItem(R.drawable.sun,
                "Icon made by: ",
                "https://www.flaticon.com/authors/freepik",
                "Freepic",
                null,null));
        infopageItemList.add(new infopageItem(R.mipmap.ic_world,
                "Icon made by: ",
                "https://www.flaticon.com/authors/freepik",
                "Freepic",
                null,null));

        ListView lf = findViewById(R.id.listview_info);
        lf.setAdapter(new infopageItem.infoPageAdapter(this,infopageItemList));

    }
}
