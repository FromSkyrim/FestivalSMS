package com.fatty.festivalsms;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.fatty.festivalsms.bean.FestivalLab;
import com.fatty.festivalsms.bean.Msg;
import com.fatty.festivalsms.fragment.FestivalCategoryFragment;

public class ChooseMessageActivity extends AppCompatActivity {

    private ListView listView;
    private FloatingActionButton floatingActionButton;
    private ArrayAdapter<Msg> mAdapter;
    private int FestivalId;
    private LayoutInflater mInflater;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_message);


        mInflater = LayoutInflater.from(this);
        FestivalId = getIntent().getIntExtra(FestivalCategoryFragment.FESTIVAL_ID, -1);

        setTitle(FestivalLab.getInstance().getFestivalById(FestivalId).getName());

        initViews();
        initEvent();
    }

    private void initEvent() {
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendMessageActivity.toActivity(ChooseMessageActivity.this, FestivalId, -1);
            }
        });
    }

    private void initViews() {
        listView = (ListView) findViewById(R.id.lv_choose_message);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab_to_send_message);

        listView.setAdapter(mAdapter = new ArrayAdapter<Msg>(this, -1, FestivalLab.getInstance().getMessageByFestivalId(FestivalId)){
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {

                if (convertView == null) {
                    convertView = mInflater.inflate(R.layout.list_view_item, parent , false);
                }
                TextView tv = (TextView) convertView.findViewById(R.id.textview_for_listview);
                Button button = (Button) convertView.findViewById(R.id.btn_toSend);

                tv.setText(getItem(position).getContent());

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SendMessageActivity.toActivity(ChooseMessageActivity.this, FestivalId, getItem(position).getId());
                    }
                });
                return convertView;
            }
        });
    }
}
