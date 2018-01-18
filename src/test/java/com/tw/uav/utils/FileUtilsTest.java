package com.tw.uav.utils;

import com.sun.xml.internal.ws.developer.MemberSubmissionAddressing;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.NoSuchElementException;

public class FileUtilsTest {
    private String filePath = "test.txt";

    @Before
    public void setup(){
        deleteFile();
    }
    @After
    public void destroy() {
        deleteFile();
    }

    @Test
    public void testFileExist() throws Exception {
        try(BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath))){
            writer.write("planeA 0 0 0");
        }
        FileUtils.checkFile(filePath);

    }

    @Test(expected = FileNotFoundException.class)
    public void testFileNotExist() throws Exception {
        FileUtils.checkFile(filePath);
    }

    @Test(expected = NoSuchElementException.class)
    public void testFileEmpty() throws Exception {
        Files.createFile(Paths.get(filePath));
        FileUtils.checkFile(filePath);
    }

    private void deleteFile() {
        if(Files.exists(Paths.get(filePath))) {
            try {
                Files.delete(Paths.get(filePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}