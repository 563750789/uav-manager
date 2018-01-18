package com.tw.uav.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;

public class FileUtils {
    public static void checkFile(String filePath) throws NoSuchElementException, FileNotFoundException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException();
        }
        if (file.length() == 0) {
            throw new NoSuchElementException();
        }
    }
}
