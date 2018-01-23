package com.mawaqaa.eatandrun.data;

public class Country {
	private String CountryCode;
	private String CountryId;
	private String CountryName;

	public Country(String countryId, String  CountryName, String countryCode) {
		setCountryName(CountryName);
		setCountryCode(countryCode);
		setCountryId(countryId);
	}

	public String getCountryCode() {
		return CountryCode;
	}

	public void setCountryCode(String countryCode) {
		CountryCode = countryCode;
	}

	public String getCountryId() {
		return CountryId;
	}

	public void setCountryId(String countryId) {
		CountryId = countryId;
	}

	public String getCountryName() {
		return CountryName;
	}

	public void setCountryName(String countryName) {
        CountryName = countryName;
	}
}


