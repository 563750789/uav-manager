package com.tw.uav.models;


import org.junit.Assert;
import org.junit.Test;

public class CoordinateTest {


    @Test
    public void testEquals() {
        Coordinate a =  new Coordinate(1,1,1);
        Coordinate b =  new Coordinate(1,1,1);
        Assert.assertTrue(a.equals(b));
    }

    @Test
    public void testNotEquals() {
        Coordinate a =  new Coordinate(1,1,1);
        Coordinate b =  new Coordinate(1,1,2);
        Assert.assertFalse(a.equals(b));
    }

    @Test
    public void testToString() {
        Coordinate a =  new Coordinate(1,1,1);
        Assert.assertEquals("1 1 1", a.toString());
    }
}
