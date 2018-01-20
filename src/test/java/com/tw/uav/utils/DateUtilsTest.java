package com.tw.uav.utils;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class DateUtilsTest {

    @Test
    public void dateTimeParserSuccess() {
        String string = new String();
        string = "2017-12-15 12:12:23";
        LocalDateTime expectedOutput = LocalDateTime.of(2017,12,15,12,12,23);
        Assert.assertEquals(expectedOutput,DateUtils.dateTimeParser(string));
    }

    @Test
    public void dateTimeParserFail(){
        String string = new String();
        string = "2017-12-1512:12:23";
        Assert.assertNull(DateUtils.dateTimeParser(string));
    }


}