package com.mawaqaa.eatandrun.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mawaqaa.eatandrun.R;
import com.mawaqaa.eatandrun.data.Country;

import java.util.ArrayList;

public class CountryNameAdapter extends BaseAdapter {
    String TAG = "CountryNameAdapter";
    Context context;
    ArrayList<Country> arrayListCountry;
    private static LayoutInflater inflater = null;


    public CountryNameAdapter(Context context,
                              ArrayList<Country> arrayListCountry) {
        this.context = context;
        this.arrayListCountry = arrayListCountry;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
             return arrayListCountry.size();

    }

    @Override
    public Country getItem(int position) {

        Country country = arrayListCountry.get(position);

            return country;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

       /* View vi = convertView;
        if (convertView == null) {
            vi = inflater.inflate(R.layout.item_country_list, null);
        }

        TextView item_CountryName = (TextView) vi.findViewById(R.id.item_countryname);

        Country countrylist = arrayListCountry.get(position);

        item_CountryName.setText(countrylist.getCountryName());
*/
		 final ViewHolder vh;
        final Country countrylist = arrayListCountry.get(position);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_country_list,
					parent, false);
			vh = new ViewHolder();
			vh.item_CountryName = (TextView) convertView
					.findViewById(R.id.item_countryname);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		 vh.item_CountryName.setText((countrylist.getCountryName()=="null")?context.getResources().getString(R.string.country)
				:countrylist.getCountryName());

		vh.item_CountryName.setText(countrylist.getCountryName());
		return convertView;
	}
	private class ViewHolder {
		// TextView tv;
		TextView item_CountryName;

     }

}
