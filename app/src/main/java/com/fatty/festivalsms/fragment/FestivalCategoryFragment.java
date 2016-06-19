package com.fatty.festivalsms.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.fatty.festivalsms.ChooseMessageActivity;
import com.fatty.festivalsms.R;
import com.fatty.festivalsms.bean.Festival;
import com.fatty.festivalsms.bean.FestivalLab;


/**
 * Created by 17255 on 2016/6/2.
 */
public class FestivalCategoryFragment extends Fragment {

    public static String FESTIVAL_ID = "festivalId";
    private GridView mGridView;
    private ArrayAdapter<Festival> mAdapter;
    private LayoutInflater mInflater;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_category_festival, container, false);

    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {

        mGridView = (GridView) view.findViewById(R.id.gridview_festival_category_fragment);

        mInflater = LayoutInflater.from(getActivity());

        mGridView.setAdapter(mAdapter = new ArrayAdapter<Festival>(getActivity(), -1, FestivalLab.getInstance().getFestivals()){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                if (convertView == null) {
                    convertView = mInflater.inflate(R.layout.item_festival, parent, false);
                }
                TextView tv = (TextView) convertView.findViewById(R.id.textview_festival_name);

                tv.setText(getItem(position).getName());

                return convertView;
            }
        });

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ChooseMessageActivity.class);
                intent.putExtra(FESTIVAL_ID, mAdapter.getItem(position).getId());
                startActivity(intent);
            }
        });
    }
}
