package com.mawaqaa.eatandrun.data;

/**
 * Created by HP on 11/27/2017.
 */

public class RestaurantListData {

    private String Res_Id;
    private String RestaurantName;
    private String Res_Address;
    private String Res_Distance;
    private String Res_Logo;
    private String Res_CityName;
    private String RatingViewCount;
    private String RatingStarCount;


    public RestaurantListData(String res_Id, String restaurantName, String res_Address, String res_Distance, String res_Logo, String res_CityName, String ratingViewCount, String ratingStarCount) {
        Res_Id = res_Id;
        RestaurantName = restaurantName;
        Res_Address = res_Address;
        Res_Distance = res_Distance;
        Res_Logo = res_Logo;
        Res_CityName = res_CityName;
        RatingViewCount = ratingViewCount;
        RatingStarCount = ratingStarCount;

    }

    public String getRes_Id() {
        return Res_Id;
    }

    public void setRes_Id(String res_Id) {
        Res_Id = res_Id;
    }

    public String getRestaurantName() {
        return RestaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        RestaurantName = restaurantName;
    }

    public String getRes_Address() {
        return Res_Address;
    }

    public void setRes_Address(String res_Address) {
        Res_Address = res_Address;
    }

    public String getRes_Distance() {
        return Res_Distance;
    }

    public void setRes_Distance(String res_Distance) {
        Res_Distance = res_Distance;
    }

    public String getRes_Logo() {
        return Res_Logo;
    }

    public void setRes_Logo(String res_Logo) {
        Res_Logo = res_Logo;
    }

    public String getRes_CityName() {
        return Res_CityName;
    }

    public void setRes_CityName(String res_CityName) {
        Res_CityName = res_CityName;
    }

    public String getRatingViewCount() {
        return RatingViewCount;
    }

    public void setRatingViewCount(String ratingViewCount) {
        RatingViewCount = ratingViewCount;
    }

    public String getRatingStarCount() {
        return RatingStarCount;
    }

    public void setRatingStarCount(String ratingStarCount) {
        RatingStarCount = ratingStarCount;
    }
}
