package com.tw.uav.models;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Signal.class})
public class PlaneTest {
    private Plane plane;

    @Before
    public void setUp() throws Exception {
        plane = new Plane();
    }

    @Test
    public void testNewPlane() {
        Assert.assertFalse(plane.isAtFault());
    }

    @Test
    public void testAddSignal() {
        plane.addSignal(new Signal());
        Assert.assertEquals(1, plane.getSignalList().size());
    }

    @Test
    public void testGetCurrCoordinate() {
        plane.setAtFault(false);
        Signal signal1 = mock(Signal.class);
        Signal signal2 = mock(Signal.class);
        Coordinate coordinate = mock(Coordinate.class);
        PowerMockito.when(signal2.calculateCurrCoordinate()).thenReturn(coordinate);
        this.plane.getSignalList().add(signal1);
        this.plane.getSignalList().add(signal2);

        Assert.assertEquals(coordinate, plane.getCurrCoordinate());
    }

    @Test
    public void testGetCurrCoordinateWithReturnNull() {
        plane.setAtFault(true);
        Signal signal1 = mock(Signal.class);
        Signal signal2 = mock(Signal.class);
        Coordinate coordinate = mock(Coordinate.class);
        this.plane.getSignalList().add(signal1);
        this.plane.getSignalList().add(signal2);

        Assert.assertNull(plane.getCurrCoordinate());
    }
}