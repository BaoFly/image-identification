package com.bf.image.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class TimeUtil {

    public static Date getCurrentTime() {
        // 设置格式化
        String format = "yyyy-MM-dd HH:mm:ss";

        // 获取当前系统时间
        LocalDateTime currentTime = LocalDateTime.now();

        // 格式化时间
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        String formattedDateTime = currentTime.format(formatter);

        // 将格式化后的时间转换为Date类型
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(formattedDateTime);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }

    public static String getCurrentTimeStr() {
        Date date = new Date(); // 当前时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = sdf.format(date);
        return formattedDate;
    }

}
