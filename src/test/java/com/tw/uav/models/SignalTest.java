package com.tw.uav.models;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SignalTest {
    private Signal signal;

    @Before
    public void setUp() throws Exception {
        signal = new Signal();
    }

    @Test
    public void testNewSignal() {
        Assert.assertFalse(signal.isInvalid());
    }
    @Test
    public void testCalculateCurrCoordinate() {
        Offset offset = new Offset(1,1,2);
        this.signal.setOffset(offset);
        Coordinate coordinate = new Coordinate(1,2,3);
        this.signal.setPreCoordinate(coordinate);

        Coordinate currCoordinate = signal.calculateCurrCoordinate();

        Assert.assertEquals(2,currCoordinate.getX());
        Assert.assertEquals(3,currCoordinate.getY());
        Assert.assertEquals(5,currCoordinate.getZ());
    }

    @Test
    public void testCalculateCurrCoordinateAndReturnNull(){
        Assert.assertNull(signal.calculateCurrCoordinate());
    }
}