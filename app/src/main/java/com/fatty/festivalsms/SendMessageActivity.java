package com.fatty.festivalsms;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fatty.festivalsms.bean.Festival;
import com.fatty.festivalsms.bean.FestivalLab;
import com.fatty.festivalsms.bean.Msg;
import com.fatty.festivalsms.bean.SentMsg;
import com.fatty.festivalsms.biz.SmsBiz;
import com.fatty.festivalsms.view.FlowLayout;

import java.util.ArrayList;
import java.util.HashSet;

public class SendMessageActivity extends AppCompatActivity {

    private static final String FESTIVAL_ID = "festivalId";
    private static final String MESSAGE_ID = "messageId";
    private static final int REQUEST_CODE = 1;

    private int mFestivalId;
    private int mMessageId;

    private EditText editTextEditMessage;
    private Button buttonAddContact;
    private FlowLayout flowLayoutContacts;
    private FloatingActionButton floatingActionButtonSendMessage;
    private View frameLayoutForProgressBar;

    private Msg mMessage;
    private Festival mFestival;

    private ArrayList<String> mContactNames = new ArrayList<String>();
    private ArrayList<String> mContactNums = new ArrayList<String>();

    private LayoutInflater mInflater;

    public static final String ACTION_SENT_MSG = "ACTION_SENT_MSG";
    public static final String ACTION_DELIVERD_MSG = "ACTION_DELIVERD_MSG";

    private PendingIntent mSentPi;
    private PendingIntent mDeliverdPi;

    private BroadcastReceiver mSentBroadcastReceiver;
    private BroadcastReceiver mDeliverdBroadcastReceiver;

    SmsBiz mSmsBiz ;

    private int mMsgSentCount;
    private int mTotalCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        mInflater = LayoutInflater.from(this);

        mSmsBiz = new SmsBiz(this);

        initData();
        initViews();
        initEvent();
        initReceivers();
    }

    private void initReceivers() {

        Intent sendIntent = new Intent(ACTION_SENT_MSG);
        mSentPi = PendingIntent.getBroadcast(this, 0, sendIntent, 0);

        Intent deliverIntent = new Intent(ACTION_DELIVERD_MSG);
        mDeliverdPi = PendingIntent.getBroadcast(this, 0, deliverIntent, 0);

        registerReceiver(mSentBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mMsgSentCount ++;
                if (getResultCode() == RESULT_OK) {
                    Log.e("TAG", "短信发送成功" + " ( " + mMsgSentCount + "/" + mTotalCount + " ) ");
                } else {
                    Log.e("TAG", "短信发送失败");
                }
                Toast.makeText(SendMessageActivity.this, "发送" + " ( " + mMsgSentCount + "/" + mTotalCount + " ) " , Toast.LENGTH_LONG).show();
                if (mMsgSentCount == mTotalCount) {
                    finish();
                }
            }
        }, new IntentFilter(ACTION_SENT_MSG));

        registerReceiver(mDeliverdBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                    Log.e("TAG", "联系人已经收到信息");
            }
        }, new IntentFilter(ACTION_DELIVERD_MSG));
    }

    private void initEvent() {

        buttonAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        //点击悬浮按钮发送短信
        floatingActionButtonSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContactNums.size() == 0) {
                    Toast.makeText(SendMessageActivity.this, "请先选择联系人", Toast.LENGTH_LONG).show();
                    return;
                }
                String msg = editTextEditMessage.getText().toString();
                if (TextUtils.isEmpty(msg)) {
                    Toast.makeText(SendMessageActivity.this, "短信内容不能为空", Toast.LENGTH_LONG).show();
                    return;
                }
                frameLayoutForProgressBar.setVisibility(View.VISIBLE);
                mTotalCount = mSmsBiz.sendMsg(mContactNums, buildSentMsg(msg), mSentPi, mDeliverdPi);
                mMsgSentCount = 0;
            }
        });
    }

    private SentMsg buildSentMsg(String msg) {

        SentMsg sentMsg = new SentMsg();
        sentMsg.setMsg(msg);
        sentMsg.setFestivalName(mFestival.getName());

        String names = "";
        for (String name : mContactNames) {
            names += name + ":";
        }

        String numbers = "";
        for (String number : mContactNums) {
            numbers += number + ":";
        }
        sentMsg.setName(names.substring(0, names.length() - 1));
        sentMsg.setNumber(numbers.substring(0, numbers.length() - 1));
        return sentMsg;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mSentBroadcastReceiver);
        unregisterReceiver(mDeliverdBroadcastReceiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            Uri contactURI = data.getData();
            Cursor cursor = getContentResolver().query(contactURI, null, null, null, null);
            cursor.moveToFirst();
            String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

            String contactNumber = getContactNumber(cursor);

            if (!TextUtils.isEmpty(contactNumber)) {
                mContactNames.add(contactName);
                mContactNums.add(contactNumber);

                addTag(contactName);
            }
        }
    }

    private void addTag(String contactName) {
        TextView textView = (TextView) mInflater.inflate(R.layout.tag, flowLayoutContacts, false);
        textView.setText(contactName);
        flowLayoutContacts.addView(textView);
    }

    private String getContactNumber(Cursor cursor) {
        int anInt = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

        String number = null;
        if (anInt == 1) {
            int contactNumber = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts._ID));

            Cursor phoneCursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactNumber, null, null );

            phoneCursor.moveToFirst();

            number = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            phoneCursor.close();
        }
        cursor.close();
        return number;
    }

    private void initViews() {
        editTextEditMessage = (EditText) findViewById(R.id.et_edit_message);
        buttonAddContact = (Button) findViewById(R.id.btn_add_contact);
        flowLayoutContacts = (FlowLayout) findViewById(R.id.flowlayout_contacts);
        floatingActionButtonSendMessage = (FloatingActionButton) findViewById(R.id.fab_send_message);
        frameLayoutForProgressBar = findViewById(R.id.framelayout_for_progressbar);

        frameLayoutForProgressBar.setVisibility(View.GONE);

        if (mMessageId != -1) {
            mMessage = FestivalLab.getInstance().getMessageById(mMessageId);
            editTextEditMessage.setText(mMessage.getContent());
        }
    }

    private void initData() {
        mFestivalId = getIntent().getIntExtra(FESTIVAL_ID, -1);
        mMessageId = getIntent().getIntExtra(MESSAGE_ID, -1);

        mFestival = FestivalLab.getInstance().getFestivalById(mFestivalId);
        setTitle(mFestival.getName());
    }

    public static void toActivity(Context context, int festivalId, int messageId) {

        Intent intent = new Intent(context, SendMessageActivity.class);

        intent.putExtra(FESTIVAL_ID, festivalId);
        intent.putExtra(MESSAGE_ID, messageId);

        context.startActivity(intent);
    }
}
