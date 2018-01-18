package com.tw.uav;

import com.tw.uav.utils.FileUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Application {

    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        try {
            String filePath = UAVManager.scanFilePath(scanner);
            FileUtils.checkFile(filePath);
            UAVManager.initPlane(filePath);
            int msgId = UAVManager.scanMsgId(scanner);
            System.out.println(UAVManager.getPlaneMsg(msgId));
        } catch (NoSuchElementException e) {
            System.out.println("Sorry, the signal file is empty, check please.");
        } catch (FileNotFoundException e) {
            System.out.println("Sorry, the signal file not exist, check please.");
        } catch (IOException e) {
            System.out.println("Sorry, file to read the file, check please.");
        }
    }
}
