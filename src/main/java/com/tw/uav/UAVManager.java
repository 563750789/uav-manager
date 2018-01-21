package com.tw.uav;

import com.tw.uav.models.Plane;
import com.tw.uav.models.Signal;
import com.tw.uav.utils.SignalParser;

import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

public class UAVManager {

    public static Plane plane;
    public static List<Plane> planeList = new ArrayList<Plane>();

    public static String scanFilePath(Scanner scanner) {
        System.out.println("please input the signal file path.");
        return scanner.nextLine();
    }

    public static void initPlane(String filePath) throws IOException {
        List<Signal> signalList = SignalParser.parse(filePath);
        plane = createPlane(signalList.remove(0));
        loadPlaneSignals(signalList);
    }

    public static void initPlanes(String filePath) throws IOException {
        HashMap<String, List<Signal>> signalListMap = SignalParser.parseSignalListMap(filePath);
        signalListMap.forEach((key, signalList) -> {
            Plane plane = createPlane(signalList.remove(0));
            loadPlaneSignals(signalList, plane);
            planeList.add(plane);
        });
    }

    public static int scanMsgId(Scanner scanner) {
        System.out.println("please input the msg id.");
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("sorry, error msg id, the msg id must be number.");
            }
        }
    }
    public static String scanPlaneId(Scanner scanner) {
        System.out.println("please input the plane id.");
        return scanner.nextLine();
    }

    public static String getPlaneMsg(int msgId) {
        if (msgId >= plane.getSignalList().size()) {
            return "Cannot find " + msgId;
        } else {
            Signal signal = plane.getSignalList().get(msgId);
            return signal.isInvalid() ? getAtFault(msgId)
                    : plane.getId() + " " + msgId + " " + signal.calculateCurrCoordinate().toString();
        }
    }

    public static String getAtFault(int msgId) {
        return "Error: " + msgId + " " + plane.getFirstInvalidSignal().getDateTime().toString();
    }


    private static Plane createPlane(Signal initialSignal) {
        correctInitialSignal(initialSignal);
        Plane plane = new Plane();
        plane.setAtFault(initialSignal.isInvalid());
        plane.setId(initialSignal.getPlaneId());
        plane.addSignal(initialSignal);
        return plane;
    }

    private static void correctInitialSignal(Signal initialSignal) {
        initialSignal.setInvalid(initialSignal.getPreCoordinate() == null);
    }

    private static void loadPlaneSignals(List<Signal> signalList) {
        signalList.forEach(signal -> {
            correctMovementSignal(signal, plane);
            if (!plane.isAtFault() && signal.isInvalid()) {
                plane.setAtFault(true);
            }
            plane.addSignal(signal);
        });
    }

    private static void loadPlaneSignals(List<Signal> signalList, Plane plane) {
        signalList.forEach(signal -> {
            correctMovementSignal(signal, plane);
            if (!plane.isAtFault() && signal.isInvalid()) {
                plane.setAtFault(true);
            }
            plane.addSignal(signal);
        });
    }

    private static void correctMovementSignal(Signal signal, Plane plane) {
        signal.setInvalid(plane.isAtFault() || signal.calculateCurrCoordinate() == null
                || !signal.getPreCoordinate().equals(plane.getCurrCoordinate()));
    }


    public static String getPlaneStatus(String planeId) {
        Stream<Plane> planeStream = planeList.stream().filter(p -> p.getId().equals(planeId));
        try {
            Plane plane = planeList.stream().filter(p -> p.getId().equals(planeId)).findFirst().get();
            return planeId + (plane.isAtFault() ? " is at fault!" : " is ok!");
        } catch (Exception e) {
            return planeId + " is not exist!";
        }
    }
}
