package com.fatty.festivalsms.bean;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 17255 on 2016/6/17.
 */
public class SentMsg {

    private int id;
    private String msg;
    private String number;
    private String name;
    private String festivalName;
    private Date date;
    private String dateStr;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public static final String TABLE_NAME = "tb_sent_msg";
    public static final String COLUMN_MSG = "msg";
    public static final String COLUMN_NUMBER = "number";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_FESTIVAL_NAME = "festivalName";
    public static final String COLUMN_DATE = "date";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFestivalName() {
        return festivalName;
    }

    public void setFestivalName(String festivalName) {
        this.festivalName = festivalName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public DateFormat getDateFormat() {
        dateStr = dateFormat.format(date);
        return dateFormat;
    }

}
