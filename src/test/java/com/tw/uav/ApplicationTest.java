package com.tw.uav;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.mockito.Mockito.when;

public class ApplicationTest {

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
    public void testSuccess(){
        String expectedOutPut = "please input the signal file path." + System.lineSeparator()
                + "please input the msg id." + System.lineSeparator()
                + "planeA 1 1 2 3" + System.lineSeparator();
        System.setIn(new ByteArrayInputStream((filePath + System.lineSeparator() + "1" + System.lineSeparator()).getBytes()));
        Application.main(null);
        Assert.assertEquals(expectedOutPut,outContent.toString());
    }

    @Test
    public void testFileNotExist(){
        String expectedOutPut = "please input the signal file path." + System.lineSeparator()
                + "Sorry, the signal file not exist, check please." + System.lineSeparator();
        System.setIn(new ByteArrayInputStream(("notExistFile.txt" + System.lineSeparator() + "1" + System.lineSeparator()).getBytes()));
        Application.main(null);
        Assert.assertEquals(expectedOutPut,outContent.toString());
    }

    @Test
    public void testFileIsEmpty() throws IOException {
        String expectedOutPut = "please input the signal file path." + System.lineSeparator()
                + "Sorry, the signal file is empty, check please." + System.lineSeparator();
        System.setIn(new ByteArrayInputStream((filePath + System.lineSeparator() + "1" + System.lineSeparator()).getBytes()));
        clearFile();
        Application.main(null);
        Assert.assertEquals(expectedOutPut,outContent.toString());
    }

    private void clearFile() throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(filePath);
        writer.print("");
        writer.close();
    }

}