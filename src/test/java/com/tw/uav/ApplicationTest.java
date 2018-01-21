package com.tw.uav;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.processing.SupportedAnnotationTypes;


public class ApplicationTest {

    private String filePath = "./signal.txt";
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();


    @Before
    public void setup() throws IOException {
        System.setOut(new PrintStream(outContent));
    }


    @After
    public void destroy() throws IOException {
    	if(Files.exists(Paths.get(filePath))) {
    		Files.delete(Paths.get(filePath));
    	}
        System.setOut(System.out);
        System.setIn(System.in);
    }

//    @Test
    public void testSuccessMsg(){
        String expectedOutPut = "please input the signal file path." + System.lineSeparator()
                + "please input the msg id." + System.lineSeparator()
                + "planeA 1 1 2 3" + System.lineSeparator();
        System.setIn(new ByteArrayInputStream((filePath + System.lineSeparator() + "1" + System.lineSeparator()).getBytes()));
        Application.main(null);
        Assert.assertEquals(expectedOutPut,outContent.toString());
    }
//    @Test
    public void testErrorMsg(){
        String expectedOutPut = "please input the signal file path." + System.lineSeparator()
                + "please input the msg id." + System.lineSeparator()
                + "Error: 5 2017-12-15T12:12:23" + System.lineSeparator();
        System.setIn(new ByteArrayInputStream((filePath + System.lineSeparator() + "5" + System.lineSeparator()).getBytes()));
        Application.main(null);
        Assert.assertEquals(expectedOutPut,outContent.toString());
    }
    @Test
    public void testPlaneOk(){
        createMultiplePlaneSignals();
        String expectedOutput = "please input the signal file path." + System.lineSeparator()
                +"please input the plane id."+ System.lineSeparator() + "planeB is ok!"+ System.lineSeparator();
        System.setIn(new ByteArrayInputStream((filePath + System.lineSeparator() + "planeB" + System.lineSeparator()).getBytes()));

        Application.main(null);

        Assert.assertEquals(expectedOutput,outContent.toString());
    }

    @Test
    public  void  testPlaneAtFault(){
        createMultiplePlaneSignals();
        String expectedOutput = "please input the signal file path." + System.lineSeparator()
                +"please input the plane id."+ System.lineSeparator() + "planeA is at fault!"+ System.lineSeparator();
        System.setIn(new ByteArrayInputStream((filePath + System.lineSeparator() + "planeA" + System.lineSeparator()).getBytes()));

        Application.main(null);

        Assert.assertEquals(expectedOutput,outContent.toString());
    }

    @Test
    public  void  testPlaneNotExsit(){
        createMultiplePlaneSignals();
        String expectedOutput = "please input the signal file path." + System.lineSeparator()
                +"please input the plane id."+ System.lineSeparator() + "planeC is not exist!"+ System.lineSeparator();
        System.setIn(new ByteArrayInputStream((filePath + System.lineSeparator() + "planeC" + System.lineSeparator()).getBytes()));

        Application.main(null);

        Assert.assertEquals(expectedOutput,outContent.toString());
    }

    @Test
    public void testFileNotExist() throws IOException {
        singlePlaneSignals();
        String expectedOutPut = "please input the signal file path." + System.lineSeparator()
                + "Sorry, the signal file not exist, check please." + System.lineSeparator();
        System.setIn(new ByteArrayInputStream(("notExistFile.txt" + System.lineSeparator()).getBytes()));
        Application.main(null);
        Assert.assertEquals(expectedOutPut,outContent.toString());
    }

    @Test
    public void testFileIsEmpty() throws IOException {
        singlePlaneSignals();
        String expectedOutPut = "please input the signal file path." + System.lineSeparator()
                + "Sorry, the signal file is empty, check please." + System.lineSeparator();
        System.setIn(new ByteArrayInputStream((filePath + System.lineSeparator()).getBytes()));
        clearFile();
        Application.main(null);
        Assert.assertEquals(expectedOutPut,outContent.toString());
    }

    private void clearFile() throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(filePath);
        writer.print("");
        writer.close();
    }




    private void singlePlaneSignals() throws IOException {
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