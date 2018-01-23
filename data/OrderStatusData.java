package com.mawaqaa.eatandrun.data;

/**
 * Created by HP on 11/27/2017.
 */

public class OrderStatusData {

    private String MenuItemId;
    private String MenuItemName;
    private String MenuItemImage;
    private String Price;
    private String qny;

    public OrderStatusData(String menuItemId, String menuItemName, String menuItemImage, String price, String qny) {
        MenuItemId = menuItemId;
        MenuItemName = menuItemName;
        MenuItemImage = menuItemImage;
        Price = price;
        this.qny = qny;
    }

    public OrderStatusData(String menuItemId, String menuItemName, String price, String qny) {
        MenuItemId = menuItemId;
        MenuItemName = menuItemName;
        Price = price;
        this.qny = qny;
    }

    public OrderStatusData(String menuItemId, String menuItemName, String price) {
        MenuItemId = menuItemId;
        MenuItemName = menuItemName;
        Price = price;
    }

    public String getMenuItemId() {
        return MenuItemId;
    }

    public void setMenuItemId(String menuItemId) {
        MenuItemId = menuItemId;
    }

    public String getMenuItemName() {
        return MenuItemName;
    }

    public String getMenuItemImage() {
        return MenuItemImage;
    }

    public void setMenuItemImage(String menuItemImage) {
        MenuItemImage = menuItemImage;
    }


    public void setMenuItemName(String menuItemName) {
        MenuItemName = menuItemName;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getQny() {
        return qny;
    }

    public void setQny(String qny) {
        this.qny = qny;
    }
}
