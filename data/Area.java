package com.mawaqaa.eatandrun.data;

public class Area {
	public String  AreaId;
	public String AreaName;


	public Area(String areaId ,String areaname )
	{
		this.AreaId = areaId;

		this.AreaName = areaname;
	}
	


	public String getAreaName(){
		return this.AreaName;
		
	}
	public String getAreaId(){
		return  AreaId;

	}

}
