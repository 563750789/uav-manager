package com.tw.uav.utils;

import com.tw.uav.models.Signal;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SignalParserTest {
    private String filePath = "signals.txt";

    @After
    public void destroy() throws IOException {
        Files.delete(Paths.get(filePath));
    }

    @Test
    public void testParse() throws IOException {
        createSinglePlaneSignals();

        List<Signal> signalList = SignalParser.parse(filePath);

        Assert.assertEquals(6, signalList.size());
        signalList.forEach(s -> {
            Assert.assertFalse(s.isInvalid());
        });
        Assert.assertEquals("0 0 0", signalList.get(0).getPreCoordinate().toString());
        Assert.assertEquals("0 0 0", signalList.get(1).getPreCoordinate().toString());
        Assert.assertEquals("1 2 3", signalList.get(2).getPreCoordinate().toString());
        Assert.assertEquals("0 3 3", signalList.get(3).getPreCoordinate().toString());
        Assert.assertNull(signalList.get(0).getOffset());
        Assert.assertEquals("1 2 3", signalList.get(1).getOffset().toString());
        Assert.assertEquals("-1 1 0", signalList.get(2).getOffset().toString());
        Assert.assertEquals("1 2 3", signalList.get(3).getOffset().toString());
        Assert.assertEquals("0 0 0", signalList.get(4).getPreCoordinate().toString());
        Assert.assertEquals("0 3 3", signalList.get(5).getPreCoordinate().toString());
        Assert.assertEquals(LocalDateTime.of(2013, 12, 11, 12, 12, 12), signalList.get(0).getDateTime());
        Assert.assertEquals(LocalDateTime.of(2013, 12, 12, 12, 12, 12), signalList.get(1).getDateTime());
        Assert.assertEquals(LocalDateTime.of(2013, 12, 13, 12, 12, 12), signalList.get(2).getDateTime());
        Assert.assertEquals(LocalDateTime.of(2013, 12, 14, 12, 12, 12), signalList.get(3).getDateTime());
        Assert.assertEquals(LocalDateTime.of(2013, 12, 15, 12, 12, 12), signalList.get(4).getDateTime());
        Assert.assertNull(signalList.get(5).getDateTime());
    }

    @Test
    public void testParseSignalListMap() throws IOException {
        createMultiplePlaneSignals();

        HashMap<String,List<Signal>> signalListMap = SignalParser.parseSignalListMap(filePath);

        Assert.assertTrue(signalListMap.keySet().size() == 3);

        Assert.assertTrue(signalListMap.keySet().contains("planeA"));
        Assert.assertEquals(4,signalListMap.get("planeA").size());
        Assert.assertEquals("1 2 3", signalListMap.get("planeA").get(3).getOffset().toString());
        Assert.assertEquals("0 3 3", signalListMap.get("planeA").get(3).getPreCoordinate().toString());

        Assert.assertTrue(signalListMap.keySet().contains("planeB"));
        Assert.assertEquals(4,signalListMap.get("planeB").size());
        Assert.assertNull(signalListMap.get("planeB").get(3).getPreCoordinate());
        Assert.assertNotNull(signalListMap.get("planeB").get(2).getPreCoordinate());
        Assert.assertNotNull(signalListMap.get("planeB").get(0).getPreCoordinate());

        Assert.assertTrue(signalListMap.keySet().contains("UNKNOW"));
        Assert.assertEquals(1,signalListMap.get("UNKNOW").size());
        Assert.assertNull(signalListMap.get("UNKNOW").get(0).getPreCoordinate());
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
            writer.write("planeB 0 0 0 2013-14-16 12:12:12");
            writer.newLine();
            writer.write("# 1 2 3 2013-14-16 12:12:12");
        } catch (IOException e) {
            Assert.fail("Fail to create file! ");
        }
    }

    private void createSinglePlaneSignals() throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath))) {
            writer.write("planeA 0 0 0 2013-12-11 12:12:12");
            writer.newLine();
            writer.write("planeA 0 0 0 1 2 3 2013-12-12 12:12:12");
            writer.newLine();
            writer.write("planeA 1 2 3  -1 1 0 2013-12-13 12:12:12");
            writer.newLine();
            writer.write("planeA 0 3 3 1 2 3 2013-12-14 12:12:12");
            writer.newLine();
            writer.write("planeA 0 0 0 1 2 3 2013-12-15 12:12:12");
            writer.newLine();
            writer.write("planeA 0 3 3 1 2 3 2013-14-16 12:12:12");
        }
    }

}


