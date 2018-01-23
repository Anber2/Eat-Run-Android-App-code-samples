package com.mawaqaa.eatandrun.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mawaqaa.eatandrun.R;
import com.mawaqaa.eatandrun.activity.EatndRunBaseActivity;

/**
 * Created by HP on 11/27/2017.
 */

public class FragmentJoinBillSplit extends EatndRunBaseFragment implements View.OnClickListener {
    String TAG = "FragmentJoinBillSplit";

    Button btn_splitjoinbill_evently, btn_joinbillsplit_chooseitem;

    private ProgressDialog progressBar;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        Activity = (EatndRunBaseActivity) this.getActivity();
    }


    public void onResume() {
        Log.d(TAG, "onResume" + this.getClass().getName());
        super.onResume();
        ((EatndRunBaseActivity) getActivity()).BaseFragment = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.join_bill_split, container, false);

        initView(v);


        return v;
    }

    private void initView(View v) {
        btn_splitjoinbill_evently = (Button) v.findViewById(R.id.btn_splitjoinbill_evently);
        btn_joinbillsplit_chooseitem = (Button) v.findViewById(R.id.btn_joinbillsplit_chooseitem);

        btn_splitjoinbill_evently.setOnClickListener(this);
        btn_joinbillsplit_chooseitem.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_splitjoinbill_evently:
                //Activity.pushFragments(new FragmenSplitBillEvently(), false, true);
                Activity.pushFragments(new JoinBillOfJoinBillSplitEvently(), false, true);
                break;
            case R.id.btn_joinbillsplit_chooseitem:
                Activity.pushFragments(new JoinBillOfJoinBillSplitChooseItem(), false, true);
                break;
        }
    }
}
