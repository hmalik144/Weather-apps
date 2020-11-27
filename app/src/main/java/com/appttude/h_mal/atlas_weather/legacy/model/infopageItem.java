package com.appttude.h_mal.atlas_weather.legacy.model;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.appttude.h_mal.atlas_weather.R;

import java.util.List;

public class infopageItem {

    private int icon;
    private String firstLine;
    private String authorUrl;
    private String authorName;
    private String siteUrl;
    private String siteName;

    public infopageItem(int icon, @Nullable String firstLine, @Nullable String authorUrl, @Nullable String authorName,
                        @Nullable String siteUrl, @Nullable String siteName) {
        this.icon = icon;
        this.firstLine = firstLine;
        this.authorUrl = authorUrl;
        this.authorName = authorName;
        this.siteUrl = siteUrl;
        this.siteName = siteName;
    }

    public int getInfoIcon() {
        return icon;
    }

    public String getFirstLine() {
        return firstLine;
    }

    public String getAuthorUrl() {
        return authorUrl;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getSiteUrl() {
        return siteUrl;
    }

    public String getSiteName() {
        return siteName;
    }

    public static class infoPageAdapter extends ArrayAdapter<infopageItem>{

        public infoPageAdapter(@NonNull Context context, @NonNull List<infopageItem> objects) {
            super(context, 0, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View listItemView = convertView;

            if (listItemView == null) {
                listItemView = LayoutInflater.from(getContext()).inflate(R.layout.info_dialog_item, parent,false);
            }

            infopageItem inf = getItem(position);

            ImageView imageView = listItemView.findViewById(R.id.infopage_icon);
            TextView firstLineTextView = listItemView.findViewById(R.id.first_part_text);
            TextView authorLinkTextView = listItemView.findViewById(R.id.author_link);
            TextView thirdLine = listItemView.findViewById(R.id.third_text);
            TextView companyWebsiteTextView = listItemView.findViewById(R.id.company_website);



            if(position == 0){
                imageView.setImageResource(inf.getInfoIcon());
                firstLineTextView.setText(inf.getFirstLine());
                authorLinkTextView.setVisibility(View.GONE);
                thirdLine.setText("");
                SetUrlHyperlink(companyWebsiteTextView,inf.getSiteName(),inf.getSiteUrl());
            }else{
                imageView.setImageResource(inf.getInfoIcon());
                firstLineTextView.setText(inf.getFirstLine());
                authorLinkTextView.setVisibility(View.VISIBLE);
                thirdLine.setText("From ");
                SetUrlHyperlink(authorLinkTextView,inf.getAuthorName(), inf.getAuthorUrl());
                SetUrlHyperlink(companyWebsiteTextView,"www.flaticon.com ","www.flaticon.com" );
            }


            return listItemView;
        }

        private void SetUrlHyperlink(TextView textView, String textString, final String url){
            textView.setText(textString);
            textView.setTextColor(getContext().getColor(android.R.color.holo_blue_dark));
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    getContext().startActivity(myIntent);
                }
            });
        }
    }
}
