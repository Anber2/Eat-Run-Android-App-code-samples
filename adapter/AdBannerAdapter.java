package com.mawaqaa.eatandrun.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mawaqaa.eatandrun.R;
import com.mawaqaa.eatandrun.data.HomeBannerClass;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by HP on 8/9/2017.
 */

public class AdBannerAdapter extends PagerAdapter {

    private ArrayList<HomeBannerClass> homeBannerClassArrayList;
    private HomeBannerClass bannerClass;
    private static LayoutInflater inflater = null;
    private Context context;
    View myImageLayout;

    public AdBannerAdapter(ArrayList<HomeBannerClass> homeBannerClassArrayList, Context context) {
        this.homeBannerClassArrayList = homeBannerClassArrayList;
        this.context = context;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        final ViewHolder vh;

        if (view == null) {
            vh = new ViewHolder();
            //view = (ViewGroup) inflater.inflate(R.layout.ad_banner_item_layout,view ,false);

             myImageLayout = inflater.inflate(R.layout.ad_banner_item_layout, view, false);
            vh.myImage = (ImageView) myImageLayout
                    .findViewById(R.id.ad_item_image);

            myImageLayout.setTag(vh);


        }else {
            vh = (ViewHolder) myImageLayout.getTag();
        }

        Picasso.with(context).load(homeBannerClassArrayList.get(position).getImgURL()).fit().into(vh.myImage);


         return view;
    }

    @Override
    public int getCount() {
        return homeBannerClassArrayList.size();
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    private class ViewHolder {

        ImageView myImage;
     }
}
