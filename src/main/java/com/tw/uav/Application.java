package com.tw.uav;

import com.tw.uav.utils.FileUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static com.tw.uav.UAVManager.*;

public class Application {

    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        try {
            String filePath = scanFilePath(scanner);
            FileUtils.checkFile(filePath);
            initPlanes(filePath);
            String planeId = scanPlaneId(scanner);
            System.out.println(getPlaneStatus(planeId));
        } catch (NoSuchElementException e) {
            System.out.println("Sorry, the signal file is empty, check please.");
        } catch (FileNotFoundException e) {
            System.out.println("Sorry, the signal file not exist, check please.");
        } catch (IOException e) {
            System.out.println("Sorry, file to read the file, check please.");
        }
    }
}
