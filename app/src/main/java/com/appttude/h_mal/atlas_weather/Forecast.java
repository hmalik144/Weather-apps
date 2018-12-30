package com.appttude.h_mal.atlas_weather;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by h_mal on 19/04/2018.
 */

public class Forecast implements Parcelable {

    private Long date_epoch;
    private Double maxtemp_c;
    private Double maxtemp_f;
    private Double mintemp_c;
    private Double mintemp_f;
    private Double avgtemp_c;
    private Double avgtemp_f;
    private Double maxwind_mph;
    private Double maxwind_kph;
    private Double totalprecip_mm;
    private Double totalprecip_in;
    private Double avgvis_km;
    private Double avgvis_miles;
    private Double avghumidity;
    private String forecast_text;
    private String iconURL;
    private Double uv;
    private String sunrise;
    private String sunset;
    private String moonrise;
    private String moonset;

    public Forecast(Long date_epoch, Double maxtemp_c, Double maxtemp_f, Double mintemp_c, Double mintemp_f, Double avgtemp_c, Double avgtemp_f,
                    Double maxwind_mph, Double maxwind_kph, Double totalprecip_mm, Double totalprecip_in, Double avgvis_km, Double avgvis_miles,
                    Double avghumidity, String forecast_text, String iconURL, Double uv, String sunrise, String sunset, String moonrise, String moonset) {
        this.date_epoch = date_epoch;
        this.maxtemp_c = maxtemp_c;
        this.maxtemp_f = maxtemp_f;
        this.mintemp_c = mintemp_c;
        this.mintemp_f = mintemp_f;
        this.avgtemp_c = avgtemp_c;
        this.avgtemp_f = avgtemp_f;
        this.maxwind_mph = maxwind_mph;
        this.maxwind_kph = maxwind_kph;
        this.totalprecip_mm = totalprecip_mm;
        this.totalprecip_in = totalprecip_in;
        this.avgvis_km = avgvis_km;
        this.avgvis_miles = avgvis_miles;
        this.avghumidity = avghumidity;
        this.forecast_text = forecast_text;
        this.iconURL = iconURL;
        this.uv = uv;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.moonrise = moonrise;
        this.moonset = moonset;
    }

    public Forecast(Long date_epoch, Double avgtemp_c,String forecast_text, String iconURL) {
        this.date_epoch = date_epoch;
        this.avgtemp_c = avgtemp_c;
        this.forecast_text = forecast_text;
        this.iconURL = iconURL;
    }

    protected Forecast(Parcel in) {
        if (in.readByte() == 0) {
            date_epoch = null;
        } else {
            date_epoch = in.readLong();
        }
        if (in.readByte() == 0) {
            maxtemp_c = null;
        } else {
            maxtemp_c = in.readDouble();
        }
        if (in.readByte() == 0) {
            maxtemp_f = null;
        } else {
            maxtemp_f = in.readDouble();
        }
        if (in.readByte() == 0) {
            mintemp_c = null;
        } else {
            mintemp_c = in.readDouble();
        }
        if (in.readByte() == 0) {
            mintemp_f = null;
        } else {
            mintemp_f = in.readDouble();
        }
        if (in.readByte() == 0) {
            avgtemp_c = null;
        } else {
            avgtemp_c = in.readDouble();
        }
        if (in.readByte() == 0) {
            avgtemp_f = null;
        } else {
            avgtemp_f = in.readDouble();
        }
        if (in.readByte() == 0) {
            maxwind_mph = null;
        } else {
            maxwind_mph = in.readDouble();
        }
        if (in.readByte() == 0) {
            maxwind_kph = null;
        } else {
            maxwind_kph = in.readDouble();
        }
        if (in.readByte() == 0) {
            totalprecip_mm = null;
        } else {
            totalprecip_mm = in.readDouble();
        }
        if (in.readByte() == 0) {
            totalprecip_in = null;
        } else {
            totalprecip_in = in.readDouble();
        }
        if (in.readByte() == 0) {
            avgvis_km = null;
        } else {
            avgvis_km = in.readDouble();
        }
        if (in.readByte() == 0) {
            avgvis_miles = null;
        } else {
            avgvis_miles = in.readDouble();
        }
        if (in.readByte() == 0) {
            avghumidity = null;
        } else {
            avghumidity = in.readDouble();
        }
        forecast_text = in.readString();
        iconURL = in.readString();
        if (in.readByte() == 0) {
            uv = null;
        } else {
            uv = in.readDouble();
        }
        sunrise = in.readString();
        sunset = in.readString();
        moonrise = in.readString();
        moonset = in.readString();
    }

    public static final Creator<Forecast> CREATOR = new Creator<Forecast>() {
        @Override
        public Forecast createFromParcel(Parcel in) {
            return new Forecast(in);
        }

        @Override
        public Forecast[] newArray(int size) {
            return new Forecast[size];
        }
    };

    public Long getDate_epoch() {
        return date_epoch;
    }

    public Double getMaxtemp_c() {
        return maxtemp_c;
    }

    public Double getMaxtemp_f() {
        return maxtemp_f;
    }

    public Double getMintemp_c() {
        return mintemp_c;
    }

    public Double getMintemp_f() {
        return mintemp_f;
    }

    public Double getAvgtemp_c() {
        return avgtemp_c;
    }

    public Double getAvgtemp_f() {
        return avgtemp_f;
    }

    public Double getMaxwind_mph() {
        return maxwind_mph;
    }

    public Double getMaxwind_kph() {
        return maxwind_kph;
    }

    public Double getTotalprecip_mm() {
        return totalprecip_mm;
    }

    public Double getTotalprecip_in() {
        return totalprecip_in;
    }

    public Double getAvgvis_km() {
        return avgvis_km;
    }

    public Double getAvgvis_miles() {
        return avgvis_miles;
    }

    public Double getAvghumidity() {
        return avghumidity;
    }

    public String getForecast_text() {
        return forecast_text;
    }

    public String getIconURL() {
        return iconURL;
    }

    public Double getUv() {
        return uv;
    }

    public String getSunrise() {
        return sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public String getMoonrise() {
        return moonrise;
    }

    public String getMoonset() {
        return moonset;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (date_epoch == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(date_epoch);
        }
        if (maxtemp_c == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(maxtemp_c);
        }
        if (maxtemp_f == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(maxtemp_f);
        }
        if (mintemp_c == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(mintemp_c);
        }
        if (mintemp_f == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(mintemp_f);
        }
        if (avgtemp_c == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(avgtemp_c);
        }
        if (avgtemp_f == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(avgtemp_f);
        }
        if (maxwind_mph == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(maxwind_mph);
        }
        if (maxwind_kph == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(maxwind_kph);
        }
        if (totalprecip_mm == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(totalprecip_mm);
        }
        if (totalprecip_in == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(totalprecip_in);
        }
        if (avgvis_km == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(avgvis_km);
        }
        if (avgvis_miles == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(avgvis_miles);
        }
        if (avghumidity == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(avghumidity);
        }
        parcel.writeString(forecast_text);
        parcel.writeString(iconURL);
        if (uv == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(uv);
        }
        parcel.writeString(sunrise);
        parcel.writeString(sunset);
        parcel.writeString(moonrise);
        parcel.writeString(moonset);
    }
}
