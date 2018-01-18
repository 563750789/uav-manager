package com.tw.uav.utils;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;

@RunWith(PowerMockRunner.class)
@PrepareForTest({File.class})
public class FileUtilsTest {
    @Test
    public void testFileExist(){
        String filePath = "usr/test.txt";
        File file = mock(File.class);
        try {
            PowerMockito.whenNew(File.class).withArguments(filePath).thenReturn(file);
            when(file.exists()).thenReturn(true);
            when(file.length()).thenReturn((long) 2);
            FileUtils.checkFile(filePath);
        } catch (Exception e) {
            Assert.fail();
        }

    }

    @Test(expected = FileNotFoundException.class)
    public void testFileNotExist(){
        String filePath = "usr/test.txt";
        File file = mock(File.class);
        try {
            PowerMockito.whenNew(File.class).withArguments(filePath).thenReturn(file);
            when(file.exists()).thenReturn(false);
            when(file.length()).thenReturn((long) 2);
            FileUtils.checkFile(filePath);
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test(expected = NoSuchElementException.class)
    public void testFileEmpty(){
        String filePath = "usr/test.txt";
        File file = mock(File.class);
        try {
            PowerMockito.whenNew(File.class).withArguments(filePath).thenReturn(file);
            when(file.exists()).thenReturn(true);
            when(file.length()).thenReturn((long) 0);
            FileUtils.checkFile(filePath);
        } catch (Exception e) {
            Assert.fail();
        }
    }
}