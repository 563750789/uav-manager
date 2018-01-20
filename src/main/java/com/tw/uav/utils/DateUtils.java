package com.tw.uav.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class DateUtils {
    public static LocalDateTime dateTimeParser(String str){
        try{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return  LocalDateTime.parse(str,formatter);
        }catch(Exception e){
            return null;
        }
    }
}
