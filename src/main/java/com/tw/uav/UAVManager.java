package com.tw.uav;

import com.tw.uav.models.Plane;
import com.tw.uav.models.Signal;
import com.tw.uav.utils.FileUtils;
import com.tw.uav.utils.SignalParser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class UAVManager {

    public static Plane plane;

    public static String scanFilePath(Scanner scanner) {
        System.out.println("please input the signal file path.");
        return scanner.nextLine();
    }

    public static void initPlane(String filePath) throws IOException {
        List<Signal> signalList = SignalParser.parse(filePath);
        createPlane(signalList.remove(0));
        loadPlaneSignals(signalList);
    }

    public static int scanMsgId(Scanner scanner) {
        System.out.println("please input the msg id.");
        while(true){
            try{
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e){
                System.out.println("sorry, error msg id, the msg id must be number.");
            }
        }
    }

    public static String getPlaneMsg(int msgId) {
        if(msgId>=plane.getSignalList().size()){
            return "Cannot find " + msgId;
        } else {
            Signal signal = plane.getSignalList().get(msgId);
            return signal.isInvalid() ? getAtFault(msgId)
                    : plane.getId() + " " + msgId + " " + signal.calculateCurrCoordinate().toString();
        }
    }

    public static String getAtFault(int msgId){
        return "Error: " + msgId +" " + plane.getFirstInvalidSignal().getDateTime().toString();
    }


    private static void createPlane(Signal initialSignal) {
        correctInitialSignal(initialSignal);
        plane = new Plane();
        plane.setAtFault(initialSignal.isInvalid());
        plane.setId(initialSignal.getPlaneId());
        plane.addSignal(initialSignal);
    }

    private static void correctInitialSignal(Signal initialSignal) {
        initialSignal.setInvalid(initialSignal.getPreCoordinate() == null);
    }

    private static void loadPlaneSignals(List<Signal> signalList){
        signalList.forEach(signal -> {
            correctMovementSignal(signal);
            if(!plane.isAtFault()&& signal.isInvalid()){
                plane.setAtFault(true);
            }
            plane.addSignal(signal);
        });
    }

    private static void correctMovementSignal(Signal signal) {
        signal.setInvalid(plane.isAtFault() || signal.calculateCurrCoordinate() == null
                || !signal.getPreCoordinate().equals(plane.getCurrCoordinate()));
    }
}
