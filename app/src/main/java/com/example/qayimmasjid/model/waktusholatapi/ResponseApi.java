package com.example.qayimmasjid.model.waktusholatapi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseApi {
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("results")
    @Expose
    private Results result;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Results getResult() {
        return result;
    }

    public void setResult(Results result) {
        this.result = result;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}

//{
//        "code": 200,
//        "status": "OK",
//        "results": {
//        "datetime": [
//        {
//        "times": {
//        "Imsak": "04:56",
//        "Sunrise": "06:28",
//        "Fajr": "05:06",
//        "Dhuhr": "13:57",
//        "Asr": "18:00",
//        "Sunset": "21:24",
//        "Maghrib": "21:45",
//        "Isha": "22:46",
//        "Midnight": "01:15"
//        },
//        "date": {
//        "timestamp": 1628121600,
//        "gregorian": "2021-08-05",
//        "hijri": "1442-12-26"
//        }
//        }
//        ],
//        "location": {
//        "latitude": 48.85661315917969,
//        "longitude": 2.352221965789795,
//        "elevation": 36.0,
//        "city": "Paris",
//        "country": "France",
//        "country_code": "FR",
//        "timezone": "Europe/Paris",
//        "local_offset": 2.0
//        },
//        "settings": {
//        "timeformat": "HH:mm",
//        "school": "Ithna Ashari",
//        "juristic": "Shafii",
//        "highlat": "None",
//        "fajr_angle": 12.0,
//        "isha_angle": 12.0
//        }
//        }
//        }