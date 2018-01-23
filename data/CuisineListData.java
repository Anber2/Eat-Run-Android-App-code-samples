package com.mawaqaa.eatandrun.data;

/**
 * Created by Admin on 6/19/2017.
 */

public class CuisineListData {

    private String CuisineId;
    private String CuisineName;
    private String CuisineDescription;
    private String CuisineImage;
    private String LanguageId;
    private String SortOrder;
    private boolean IsActive;
    private String MenuItemCount;

    public CuisineListData(String cuisineId, String cuisineName, String cuisineDescription, String cuisineImage, String languageId, String sortOrder, boolean isActive, String menuItemCount) {
        CuisineId = cuisineId;
        CuisineName = cuisineName;
        CuisineDescription = cuisineDescription;
        CuisineImage = cuisineImage;
        LanguageId = languageId;
        SortOrder = sortOrder;
        IsActive = isActive;
        MenuItemCount = menuItemCount;
    }

    public String getCuisineId() {
        return CuisineId;
    }

    public String getCuisineName() {
        return CuisineName;
    }

    public String getCuisineDescription() {
        return CuisineDescription;
    }

    public String getCuisineImage() {
        return CuisineImage;
    }

    public String getLanguageId() {
        return LanguageId;
    }

    public String getSortOrder() {
        return SortOrder;
    }

    public boolean isActive() {
        return IsActive;
    }

    public String getMenuItemCount() {
        return MenuItemCount;
    }

    public void setCuisineId(String cuisineId) {
        CuisineId = cuisineId;
    }

    public void setCuisineName(String cuisineName) {
        CuisineName = cuisineName;
    }

    public void setCuisineDescription(String cuisineDescription) {
        CuisineDescription = cuisineDescription;
    }

    public void setCuisineImage(String cuisineImage) {
        CuisineImage = cuisineImage;
    }

    public void setLanguageId(String languageId) {
        LanguageId = languageId;
    }

    public void setSortOrder(String sortOrder) {
        SortOrder = sortOrder;
    }

    public void setActive(boolean active) {
        IsActive = active;
    }

    public void setMenuItemCount(String menuItemCount) {
        MenuItemCount = menuItemCount;
    }
}
