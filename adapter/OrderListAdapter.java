package com.mawaqaa.eatandrun.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mawaqaa.eatandrun.R;

/**
 * Created by HP on 11/27/2017.
 */
public class OrderListAdapter extends BaseAdapter {

    private String TAG = "LMyporpAdapter";
    Context context;
  //  LandlordMypropertylistData lmyporpData;
    //ArrayList<LandlordMypropertylistData> LandlordMypropertylistData;
    LayoutInflater layoutInflater;
   // LPropertyIdSelected lpropinterfce;

   // ViewHolder viewHolder;


    TextView Propertyname,UniqueId,lbl_Tcount;
    int selectedIndex= -1;
    public OrderListAdapter(){

    }
    public OrderListAdapter(Context context){
this.context = context;
    }

   /* public RestaurantListAdapter(Context context, ArrayList<LandlordMypropertylistData> LandlordMypropertylistData, LPropertyIdSelected lpropinterfce
                                    ){
        this.context = context;
        this.LandlordMypropertylistData = LandlordMypropertylistData;
        this.lpropinterfce = lpropinterfce;


    }
*/
    @Override
    public int getCount() {
        //return LandlordMypropertylistData.size();
        return 3;
    }

    @Override
    public Object getItem(int position) {
        //return LandlordMypropertylistData.get(position);
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public void setSelectedIndex(int index){
        selectedIndex =  index;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       // lmyporpData = LandlordMypropertylistData.get(position);
        if(convertView ==  null) {
            // viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.list_item_order, null);
        }
       /*     //viewHolder.imageView = (ImageView)convertView.findViewById(R.id.imgViewRelProd);
        final CheckBox chkbox = (CheckBox) convertView.findViewById(R.id.checkBoxPropertylist);
    Propertyname = (TextView) convertView.findViewById(R.id.txt_propertlist_MyProp);
       UniqueId = (TextView) convertView.findViewById(R.id.txt_Uniqueid_MyProp);
        lbl_Tcount = (TextView) convertView.findViewById(R.id.lbl_Tcount);

         Propertyname.setText(lmyporpData.getPropertyName());
         UniqueId.setText(lmyporpData.getUniqueId());
        lbl_Tcount.setText(lmyporpData.getTcount());*/



        return convertView;
    }


   /* class ViewHolder{

        CheckBox chkbox;
        TextView Propertyname,UniqueId;

    }*/
}
