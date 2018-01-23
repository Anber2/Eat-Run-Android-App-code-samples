package com.mawaqaa.eatandrun.data;

/**
 * Created by HP on 8/22/2017.
 */

public class CityData {

    public String  CityId;
    public String CityName;


    public CityData(String cityId, String cityName) {
        CityId = cityId;
        CityName = cityName;
    }

    public void setCityId(String cityId) {
        CityId = cityId;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }

    public String getCityId() {
        return CityId;
    }

    public String getCityName() {
        return CityName;
    }
}
