package seonghunjo.com.lifelogger;

import android.graphics.drawable.Drawable;
import android.text.format.DateUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Seonghun on 2015-12-19.
 */


public class Log {

    public enum LogType {
        TYPE_NONE,
        TYPE_MEAL,
        TYPE_CAFE,
        TYPE_STUDY,
        TYPE_MEET
    }

    // 아이콘
    public Drawable mIcon;
    // DB Index
    public int id;
    // 위치
    public double lat, lng;
    // 제목
    public String title;
    // 날짜
    public String date;
    // 내용
    public String content;
    // 타입
    public LogType type;

    public Log(double logLat, double logLng, String logTitle, String logContent) {
        lat = logLat;
        lng = logLng;
        title = logTitle;
        content = logContent;
        type = LogType.TYPE_NONE;

        DateFormat sdFormat = new SimpleDateFormat("yyyy. MM. dd hh:mm:ss a");
        Date nowDate = new Date();
        date = sdFormat.format(nowDate);
    }

    public Log(double logLat, double logLng, String logDate, String logTitle, String logContent) {
        lat = logLat;
        lng = logLng;
        title = logTitle;
        content = logContent;
        type = LogType.TYPE_NONE;
        date = logDate;
    }

    public Log(double logLat, double logLng, String logTitle, String logContent, LogType logType) {
        lat = logLat;
        lng = logLng;
        title = logTitle;
        content = logContent;
        type = logType;

        DateFormat sdFormat = new SimpleDateFormat("yyyy. MM. dd hh:mm:ss a");
        Date nowDate = new Date();
        date = sdFormat.format(nowDate);
    }

    public Log(double logLat, double logLng, String logDate, String logTitle, String logContent, LogType logType) {
        lat = logLat;
        lng = logLng;
        title = logTitle;
        content = logContent;
        type = logType;
        date = logDate;
    }
}
