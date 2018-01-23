package com.mawaqaa.eatandrun.data;

/**
 * Created by HP on 11/27/2017.
 */

public class RestaurantMenuData {

    private String MenuSectionId;
    private String MenuSectionName;
    private String LanguageId;
    private String SortOrder;
    private String IsActive;
    private String MenuSectionCount;
    private String Logo;

    public void setLogo(String logo) {
        Logo = logo;
    }

    public String getLogo() {
        return Logo;
    }

    public RestaurantMenuData(String menuSectionId, String menuSectionName, String languageId, String sortOrder, String isActive, String menuSectionCount, String logo) {
        MenuSectionId = menuSectionId;
        MenuSectionName = menuSectionName;
        LanguageId = languageId;
        SortOrder = sortOrder;
        IsActive = isActive;
        MenuSectionCount = menuSectionCount;
        Logo = logo;
    }

    public void setMenuSectionId(String menuSectionId) {
        MenuSectionId = menuSectionId;
    }

    public void setMenuSectionName(String menuSectionName) {
        MenuSectionName = menuSectionName;
    }

    public void setLanguageId(String languageId) {
        LanguageId = languageId;
    }

    public void setSortOrder(String sortOrder) {
        SortOrder = sortOrder;
    }

    public void setIsActive(String isActive) {
        IsActive = isActive;
    }

    public void setMenuSectionCount(String menuSectionCount) {
        MenuSectionCount = menuSectionCount;
    }


    public String getMenuSectionId() {
        return MenuSectionId;
    }

    public String getMenuSectionName() {
        return MenuSectionName;
    }

    public String getLanguageId() {
        return LanguageId;
    }

    public String getSortOrder() {
        return SortOrder;
    }

    public String getIsActive() {
        return IsActive;
    }

    public String getMenuSectionCount() {
        return MenuSectionCount;
    }

 }
