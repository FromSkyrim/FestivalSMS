package com.fatty.festivalsms.biz;

import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.telephony.SmsManager;

import com.fatty.festivalsms.bean.Msg;
import com.fatty.festivalsms.bean.SentMsg;
import com.fatty.festivalsms.db.SmsProvider;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by 17255 on 2016/6/17.
 */
public class SmsBiz {

    Context context;

    public SmsBiz(Context context) {
        this.context = context;
    }

    public int sendMsg(String number, String msg, PendingIntent sentPi, PendingIntent deliveryPi) {

        SmsManager smsManager = SmsManager.getDefault();
        ArrayList<String> contents = smsManager.divideMessage(msg);
        for (String content : contents) {
            smsManager.sendTextMessage(number, null, content, sentPi, deliveryPi);
        }
        return contents.size();
    }

    public int sendMsg(ArrayList<String> numbers, SentMsg msg, PendingIntent sentPi, PendingIntent deliveryPi) {

        save(msg);
        int result = 0;
        for (String number : numbers) {
            int count = sendMsg(number, msg.getMsg(), sentPi, deliveryPi);
            result += count;
        }
        return result;
    }

    private void save(SentMsg sentMsg) {
        sentMsg.setDate(new Date());
        ContentValues values = new ContentValues();
        values.put(SentMsg.COLUMN_DATE, sentMsg.getDate().getTime());
        values.put(SentMsg.COLUMN_FESTIVAL_NAME, sentMsg.getFestivalName());
        values.put(SentMsg.COLUMN_MSG, sentMsg.getMsg());
        values.put(SentMsg.COLUMN_NAME, sentMsg.getName());
        values.put(SentMsg.COLUMN_NUMBER, sentMsg.getNumber());

        context.getContentResolver().insert(SmsProvider.URI_SMS_ALL, values);
    }
}
