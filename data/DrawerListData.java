package com.mawaqaa.eatandrun.data;

public class DrawerListData {

	private String name;
	private int imageId;

	public DrawerListData(){

	}
	public DrawerListData(String name, int imageId){
		this.name = name;
		this.imageId = imageId;
	}

	public int getImageId() {
		return imageId;
	}

	public void setImageId(int imageId) {
		this.imageId = imageId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
