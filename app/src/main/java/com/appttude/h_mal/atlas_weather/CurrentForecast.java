package com.appttude.h_mal.atlas_weather;

import android.os.Parcel;
import android.os.Parcelable;

public class CurrentForecast implements Parcelable{

    private String location;
    private Double latitude;
    private Double longitude;
    private int last_updated_epoch;
    private Double temp_c;
    private Double temp_f;
    private String condition_text;
    private String iconURL;
    private Double wind_mph;
    private Double wind_kph;
    private String wind_dir;
    private Double pressure_mb;
    private Double pressure_in;
    private Double precip_mm;
    private Double precip_in;
    private Double humidity;
    private Double cloud;
    private Double feelslike_c;
    private Double feelslike_f;
    private Double vis_km;
    private Double vis_miles;

    public CurrentForecast() {
    }

    public CurrentForecast(String location, Double latitude, Double longitude, int last_updated_epoch,
                           Double temp_c, Double temp_f, String condition_text, String iconURL, Double wind_mph,
                           Double wind_kph, String wind_dir, Double pressure_mb, Double pressure_in, Double precip_mm,
                           Double precip_in, Double humidity, Double cloud, Double feelslike_c, Double feelslike_f,
                           Double vis_km, Double vis_miles) {
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.last_updated_epoch = last_updated_epoch;
        this.temp_c = temp_c;
        this.temp_f = temp_f;
        this.condition_text = condition_text;
        this.iconURL = iconURL;
        this.wind_mph = wind_mph;
        this.wind_kph = wind_kph;
        this.wind_dir = wind_dir;
        this.pressure_mb = pressure_mb;
        this.pressure_in = pressure_in;
        this.precip_mm = precip_mm;
        this.precip_in = precip_in;
        this.humidity = humidity;
        this.cloud = cloud;
        this.feelslike_c = feelslike_c;
        this.feelslike_f = feelslike_f;
        this.vis_km = vis_km;
        this.vis_miles = vis_miles;
    }

    public String getLocation() {
        return location;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public int getLast_updated_epoch() {
        return last_updated_epoch;
    }

    public Double getTemp_c() {
        return temp_c;
    }

    public Double getTemp_f() {
        return temp_f;
    }

    public String getCondition_text() {
        return condition_text;
    }

    public String getIconURL() {
        return iconURL;
    }

    public Double getWind_mph() {
        return wind_mph;
    }

    public Double getWind_kph() {
        return wind_kph;
    }

    public String getWind_dir() {
        return wind_dir;
    }

    public Double getPressure_mb() {
        return pressure_mb;
    }

    public Double getPressure_in() {
        return pressure_in;
    }

    public Double getPrecip_mm() {
        return precip_mm;
    }

    public Double getPrecip_in() {
        return precip_in;
    }

    public Double getHumidity() {
        return humidity;
    }

    public Double getCloud() {
        return cloud;
    }

    public Double getFeelslike_c() {
        return feelslike_c;
    }

    public Double getFeelslike_f() {
        return feelslike_f;
    }

    public Double getVis_km() {
        return vis_km;
    }

    public Double getVis_miles() {
        return vis_miles;
    }

    public static Creator<CurrentForecast> getCREATOR() {
        return CREATOR;
    }

    protected CurrentForecast(Parcel in) {
        location = in.readString();
        if (in.readByte() == 0) {
            latitude = null;
        } else {
            latitude = in.readDouble();
        }
        if (in.readByte() == 0) {
            longitude = null;
        } else {
            longitude = in.readDouble();
        }
        last_updated_epoch = in.readInt();
        if (in.readByte() == 0) {
            temp_c = null;
        } else {
            temp_c = in.readDouble();
        }
        if (in.readByte() == 0) {
            temp_f = null;
        } else {
            temp_f = in.readDouble();
        }
        condition_text = in.readString();
        iconURL = in.readString();
        if (in.readByte() == 0) {
            wind_mph = null;
        } else {
            wind_mph = in.readDouble();
        }
        if (in.readByte() == 0) {
            wind_kph = null;
        } else {
            wind_kph = in.readDouble();
        }
        wind_dir = in.readString();
        if (in.readByte() == 0) {
            pressure_mb = null;
        } else {
            pressure_mb = in.readDouble();
        }
        if (in.readByte() == 0) {
            pressure_in = null;
        } else {
            pressure_in = in.readDouble();
        }
        if (in.readByte() == 0) {
            precip_mm = null;
        } else {
            precip_mm = in.readDouble();
        }
        if (in.readByte() == 0) {
            precip_in = null;
        } else {
            precip_in = in.readDouble();
        }
        if (in.readByte() == 0) {
            humidity = null;
        } else {
            humidity = in.readDouble();
        }
        if (in.readByte() == 0) {
            cloud = null;
        } else {
            cloud = in.readDouble();
        }
        if (in.readByte() == 0) {
            feelslike_c = null;
        } else {
            feelslike_c = in.readDouble();
        }
        if (in.readByte() == 0) {
            feelslike_f = null;
        } else {
            feelslike_f = in.readDouble();
        }
        if (in.readByte() == 0) {
            vis_km = null;
        } else {
            vis_km = in.readDouble();
        }
        if (in.readByte() == 0) {
            vis_miles = null;
        } else {
            vis_miles = in.readDouble();
        }
    }

    public static final Creator<CurrentForecast> CREATOR = new Creator<CurrentForecast>() {
        @Override
        public CurrentForecast createFromParcel(Parcel in) {
            return new CurrentForecast(in);
        }

        @Override
        public CurrentForecast[] newArray(int size) {
            return new CurrentForecast[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(location);
        if (latitude == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(latitude);
        }
        if (longitude == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(longitude);
        }
        parcel.writeInt(last_updated_epoch);
        if (temp_c == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(temp_c);
        }
        if (temp_f == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(temp_f);
        }
        parcel.writeString(condition_text);
        parcel.writeString(iconURL);
        if (wind_mph == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(wind_mph);
        }
        if (wind_kph == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(wind_kph);
        }
        parcel.writeString(wind_dir);
        if (pressure_mb == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(pressure_mb);
        }
        if (pressure_in == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(pressure_in);
        }
        if (precip_mm == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(precip_mm);
        }
        if (precip_in == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(precip_in);
        }
        if (humidity == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(humidity);
        }
        if (cloud == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(cloud);
        }
        if (feelslike_c == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(feelslike_c);
        }
        if (feelslike_f == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(feelslike_f);
        }
        if (vis_km == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(vis_km);
        }
        if (vis_miles == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(vis_miles);
        }
    }
}
