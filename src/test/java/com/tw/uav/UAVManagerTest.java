package com.tw.uav;

import com.tw.uav.models.Plane;
import com.tw.uav.models.Signal;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class UAVManagerTest {

    private String filePath = "./signal.txt";
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayInputStream inContent = new ByteArrayInputStream(filePath.getBytes());


    @Before
    public void setup() throws IOException {
        System.setOut(new PrintStream(outContent));
        System.setIn(inContent);
    }

    @After
    public void destroy() throws IOException {
    	if(Files.exists(Paths.get(filePath))) {
    		Files.delete(Paths.get(filePath));
    	}       
        System.setOut(System.out);
        System.setIn(System.in);
    }

    @Test
    public void testScanFilePath() {
        Assert.assertEquals(filePath,UAVManager.scanFilePath(new Scanner(System.in)));
        Assert.assertEquals("please input the signal file path." + System.lineSeparator(), outContent.toString());
    }

    @Test
    public void testInitPlane() throws IOException {
        createSinglePlaneSignals();

        UAVManager.initPlane(filePath);

        Plane plane = UAVManager.plane;
        Assert.assertEquals("planeA", plane.getId());
        Assert.assertEquals(6, plane.getSignalList().size());
        Assert.assertTrue(plane.isAtFault());
        Assert.assertFalse(plane.getSignalList().get(0).isInvalid());
        Assert.assertFalse(plane.getSignalList().get(1).isInvalid());
        Assert.assertFalse(plane.getSignalList().get(2).isInvalid());
        Assert.assertFalse(plane.getSignalList().get(3).isInvalid());
        Assert.assertTrue(plane.getSignalList().get(4).isInvalid());
        Assert.assertTrue(plane.getSignalList().get(5).isInvalid());
    }

    @Test
    public void testInitPlanes() throws IOException {
        createMultiplePlaneSignals();

        UAVManager.initPlanes(filePath);

        List<Plane> planeList = UAVManager.planeList;

        Assert.assertEquals(3,planeList.size());
        Assert.assertTrue(planeList.stream().map(plane -> plane.getId()).collect(Collectors.toList()).contains("planeA"));
        Assert.assertTrue(planeList.stream().map(plane -> plane.getId()).collect(Collectors.toList()).contains("planeB"));
        Assert.assertEquals(4,planeList.stream().filter(x -> x.getId().equals("planeA")).findFirst().get().getSignalList().size());
        Assert.assertEquals(3,planeList.stream().filter(x -> x.getId().equals("planeB")).findFirst().get().getSignalList().size());
    }

    @Test
    public void testScanMsgId() {
        String expectedOutPut = "please input the msg id." + System.lineSeparator() + "sorry, error msg id, the msg id must be number." + System.lineSeparator()
                + "sorry, error msg id, the msg id must be number." + System.lineSeparator();
        System.setIn(new ByteArrayInputStream(("dd" + System.lineSeparator() + "aa" + System.lineSeparator() + "1").getBytes()));
        Assert.assertEquals(1,UAVManager.scanMsgId(new Scanner(System.in)));
        Assert.assertEquals(expectedOutPut, outContent.toString());
    }

    @Test
    public void testScanPlaneId() {
        createMultiplePlaneSignals();
        String expectedOutPut = "please input the plane id." + System.lineSeparator() ;
        System.setIn(new ByteArrayInputStream(("planeA"+ System.lineSeparator()).getBytes()));
        Assert.assertEquals("planeA",UAVManager.scanPlaneId(new Scanner(System.in)));
        Assert.assertEquals(expectedOutPut,outContent.toString());
    }

    @Test
    public void testGetMsg() throws IOException {
        createSinglePlaneSignals();

        UAVManager.initPlane(filePath);

        Assert.assertEquals("planeA 0 0 0 0",UAVManager.getPlaneMsg(0));
        Assert.assertEquals("planeA 1 1 2 3",UAVManager.getPlaneMsg(1));
        Assert.assertEquals("planeA 2 0 3 3",UAVManager.getPlaneMsg(2));
        Assert.assertEquals("planeA 3 1 5 6",UAVManager.getPlaneMsg(3));
        Assert.assertEquals("Error: 4 2017-12-15T12:12:23",UAVManager.getPlaneMsg(4));
        Assert.assertEquals("Error: 5 2017-12-15T12:12:23",UAVManager.getPlaneMsg(5));
        Assert.assertEquals("Cannot find 6",UAVManager.getPlaneMsg(6));
    }
    @Test
    public void testGetAtFault() throws IOException {
        createSinglePlaneSignals();

        UAVManager.initPlane(filePath);

        Assert.assertEquals("Error: 4 2017-12-15T12:12:23",UAVManager.getAtFault(4));
        Assert.assertEquals("Error: 5 2017-12-15T12:12:23",UAVManager.getAtFault(5));
    }

    @Test
    public void testGetPlaneStatus() throws IOException {
        String notExistPlaneId = "notExistPlaneId";
        String atFaultPlaneId = "planeA";
        String okPlaneId = "planeB";
        createMultiplePlaneSignals();
        UAVManager.initPlanes(filePath);

        Assert.assertEquals(notExistPlaneId + " is not exist!", UAVManager.getPlaneStatus(notExistPlaneId));
        Assert.assertEquals(atFaultPlaneId + " is at fault!", UAVManager.getPlaneStatus(atFaultPlaneId));
        Assert.assertEquals(okPlaneId + " is ok!", UAVManager.getPlaneStatus(okPlaneId));
    }




    private void createSinglePlaneSignals() throws IOException {
        try(BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath))){
            writer.write("planeA 0 0 0 2017-12-13 12:12:23");
            writer.newLine();
            writer.write("planeA 0 0 0 1 2 3 2017-12-13 12:43:21");
            writer.newLine();
            writer.write("planeA 1 2 3 -1 1 0 2017-11-13 12:12:23");
            writer.newLine();
            writer.write("planeA 0 3 3 1 2 3 2017-12-13 11:12:23");
            writer.newLine();
            writer.write("planeA 0 0 0 1 2 3 2017-12-15 12:12:23");
            writer.newLine();
            writer.write("planeA 0 3 3 1 2 3 2017-12-17 12:22:23");
        }
    }

    private void createMultiplePlaneSignals() {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath))) {
            writer.write("planeA 0 0 0 2013-12-11 12:12:12");
            writer.newLine();
            writer.write("planeB 0 0 0 2013-12-12 12:12:12");
            writer.newLine();
            writer.write("planeB 0 0 0 -1 1 0 2013-12-13 12:12:12");
            writer.newLine();
            writer.write("planeB -1 1 0 1 2 3 2013-12-14 12:12:12");
            writer.newLine();
            writer.write("planeA 0 0 0 1 2 3 2013-14-16 12:12:12");
            writer.newLine();
            writer.write("planeA 1 2 3 1 1 1 2013-14-16 12:12:12");
            writer.newLine();
            writer.write("planeA 0 3 3 1 2 3 2013-14-16 12:12:12");
            writer.newLine();
            writer.write("# 1 2 3 2013-14-16 12:12:12");
        } catch (IOException e) {
            Assert.fail("Fail to create file! ");
        }
    }

}