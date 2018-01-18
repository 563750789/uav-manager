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
import java.util.Scanner;

import static org.junit.Assert.*;

public class UAVManagerTest {

    private String filePath = "signal.txt";
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayInputStream inContent = new ByteArrayInputStream(filePath.getBytes());


    @Before
    public void setup() throws IOException {
        System.setOut(new PrintStream(outContent));
        System.setIn(inContent);
        try(BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath))){
            writer.write("planeA 0 0 0");
            writer.newLine();
            writer.write("planeA 0 0 0 1 2 3");
            writer.newLine();
            writer.write("planeA 1 2 3  -1 1 0");
            writer.newLine();
            writer.write("planeA 0 3 3 1 2 3");
            writer.newLine();
            writer.write("planeA 0 0 0 1 2 3");
            writer.newLine();
            writer.write("planeA 0 3 3 1 2 3");
        }
    }

    @After
    public void destroy() throws IOException {
        Files.delete(Paths.get(filePath));
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
    public void testScanMsgId() {
        String expectedOutPut = "please input the msg id." + System.lineSeparator() + "sorry, error msg id, the msg id must be number." + System.lineSeparator()
                + "sorry, error msg id, the msg id must be number." + System.lineSeparator();
        System.setIn(new ByteArrayInputStream(("dd" + System.lineSeparator() + "aa" + System.lineSeparator() + "1").getBytes()));
        Assert.assertEquals(1,UAVManager.scanMsgId(new Scanner(System.in)));
        Assert.assertEquals(expectedOutPut, outContent.toString());
    }

    @Test
    public void getMsg() throws IOException {
        UAVManager.initPlane(filePath);
        Plane plane = UAVManager.plane;

        Assert.assertEquals("planeA 0 0 0 0",UAVManager.getPlaneMsg(0));
        Assert.assertEquals("planeA 1 1 2 3",UAVManager.getPlaneMsg(1));
        Assert.assertEquals("planeA 2 0 3 3",UAVManager.getPlaneMsg(2));
        Assert.assertEquals("planeA 3 1 5 6",UAVManager.getPlaneMsg(3));
        Assert.assertEquals("Error 4",UAVManager.getPlaneMsg(4));
        Assert.assertEquals("Error 5",UAVManager.getPlaneMsg(5));
        Assert.assertEquals("Cannot find 6",UAVManager.getPlaneMsg(6));
    }
}