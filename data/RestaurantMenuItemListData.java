package com.mawaqaa.eatandrun.data;

/**
 * Created by HP on 11/27/2017.
 */

public class RestaurantMenuItemListData {


    private String MenuItemId;
    private String MenuItemName;
    private String MenuItemDescription;
    private String MenuItemImage;
    private String Price;

    public RestaurantMenuItemListData(String menuItemId, String menuItemName, String menuItemDescription, String menuItemImage, String price) {
        MenuItemId = menuItemId;
        MenuItemName = menuItemName;
        MenuItemDescription = menuItemDescription;
        MenuItemImage = menuItemImage;
        Price = price;
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

    public void setMenuItemImage(String menuItemImage) {
        MenuItemImage = menuItemImage;
    }

    public void setPrice(String price) {
        Price = price;
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

    public String getMenuItemImage() {
        return MenuItemImage;
    }

    public String getPrice() {
        return Price;
    }
}
