package com.mawaqaa.eatandrun.data;

/**
 * Created by HP on 11/27/2017.
 */

public class HomeBannerClass {

    private String imgURL;
    private String NavigateUrl;

    public HomeBannerClass(String imgURL, String navigateUrl) {
        this.imgURL = imgURL;
        NavigateUrl = navigateUrl;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public String getNavigateUrl() {
        return NavigateUrl;
    }

    public void setNavigateUrl(String navigateUrl) {
        NavigateUrl = navigateUrl;
    }

}
