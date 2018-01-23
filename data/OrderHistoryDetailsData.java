package com.mawaqaa.eatandrun.data;

/**
 * Created by HP on 11/27/2017.
 */

public class OrderHistoryDetailsData {


    private String MenuItemImage;
    private String MenuItemName;
    private String MenuItemPrice;

    public OrderHistoryDetailsData(String menuItemImage, String menuItemName, String menuItemPrice) {
        MenuItemImage = menuItemImage;
        MenuItemName = menuItemName;
        MenuItemPrice = menuItemPrice;
    }

    public String getMenuItemImage() {
        return MenuItemImage;
    }

    public String getMenuItemName() {
        return MenuItemName;
    }

    public String getMenuItemPrice() {
        return MenuItemPrice;
    }

    public void setMenuItemImage(String menuItemImage) {
        MenuItemImage = menuItemImage;
    }

    public void setMenuItemName(String menuItemName) {
        MenuItemName = menuItemName;
    }

    public void setMenuItemPrice(String menuItemPrice) {
        MenuItemPrice = menuItemPrice;
    }
}
