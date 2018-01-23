package com.mawaqaa.eatandrun.data;

/**
 * Created by HP on 11/27/2017.
 */

public class RestaurantOfferItemListData {

    private String MenuItemId;
    private String MenuItemName;
    private String MenuItemDescription;
    private String MenuSectionId;
    private String MenuSectionName;
    private String MenuItemImage;
    private String Price;
    private String Discount;
    private String AvailableFrom;
    private String AvailableTo;
    private String IsActive;
    private String Message;

    public RestaurantOfferItemListData(String menuItemId, String menuItemName, String menuItemDescription, String menuSectionId, String menuSectionName, String menuItemImage, String price, String discount, String availableFrom, String availableTo, String isActive, String message) {
        MenuItemId = menuItemId;
        MenuItemName = menuItemName;
        MenuItemDescription = menuItemDescription;
        MenuSectionId = menuSectionId;
        MenuSectionName = menuSectionName;
        MenuItemImage = menuItemImage;
        Price = price;
        Discount = discount;
        AvailableFrom = availableFrom;
        AvailableTo = availableTo;
        IsActive = isActive;
        Message = message;
    }

    public String getMenuItemId() {
        return MenuItemId;
    }

    public String getMenuItemName() {
        return MenuItemName;
    }

    public String getMenuItemDescription() {
        return MenuItemDescription;
    }

    public String getMenuSectionId() {
        return MenuSectionId;
    }

    public String getMenuSectionName() {
        return MenuSectionName;
    }

    public String getMenuItemImage() {
        return MenuItemImage;
    }

    public String getPrice() {
        return Price;
    }

    public String getDiscount() {
        return Discount;
    }

    public String getAvailableFrom() {
        return AvailableFrom;
    }

    public String getAvailableTo() {
        return AvailableTo;
    }

    public String getIsActive() {
        return IsActive;
    }

    public String getMessage() {
        return Message;
    }

    public void setMenuItemId(String menuItemId) {

        MenuItemId = menuItemId;
    }

    public void setMenuItemName(String menuItemName) {
        MenuItemName = menuItemName;
    }

    public void setMenuItemDescription(String menuItemDescription) {
        MenuItemDescription = menuItemDescription;
    }

    public void setMenuSectionId(String menuSectionId) {
        MenuSectionId = menuSectionId;
    }

    public void setMenuSectionName(String menuSectionName) {
        MenuSectionName = menuSectionName;
    }

    public void setMenuItemImage(String menuItemImage) {
        MenuItemImage = menuItemImage;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public void setAvailableFrom(String availableFrom) {
        AvailableFrom = availableFrom;
    }

    public void setAvailableTo(String availableTo) {
        AvailableTo = availableTo;
    }

    public void setIsActive(String isActive) {
        IsActive = isActive;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
