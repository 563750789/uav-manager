package com.tw.uav.utils;

import com.tw.uav.models.Coordinate;
import com.tw.uav.models.Offset;
import com.tw.uav.models.Signal;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SignalParser {
    private final static String INITIAL_SIGNAL_PATTERN = "^([a-zA-Z0-9]+)\\s+(-?\\d+)\\s+(-?\\d+)\\s+(-?\\d+)\\s+(\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2})$";
    private final static String SIGNAL_PLANE_PATTERN = "^([a-zA-Z0-9]+)\\s+.*";
    private final static String MOVEMENT_SIGNAL_PATTERN = "^([a-zA-Z0-9]+)\\s+(-?\\d+)\\s+(-?\\d+)\\s+(-?\\d+)\\s+(-?\\d+)\\s+(-?\\d+)\\s+(-?\\d+)\\s+(\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2})$";

    public static List<Signal> parse(String filePath) throws IOException {
        List<Signal> signalList = new ArrayList<Signal>();
        signalList.add(parseInitialSignal(filePath));
        signalList.addAll(parseMovementSignals(filePath));

        return signalList;
    }

    private static Signal parseInitialSignal(String filePath) {
        Pattern pattern = Pattern.compile(INITIAL_SIGNAL_PATTERN);
        Matcher matcher;
        Signal signal;
        try (Stream<String> stringStream = Files.lines(Paths.get(filePath))) {
            matcher = pattern.matcher(stringStream.findFirst().get());
            signal = new Signal();
            if (matcher.find()) {
                signal.setPlaneId(matcher.group(1));
                signal.setPreCoordinate(new Coordinate(Integer.parseInt(matcher.group(2)),
                        Integer.parseInt(matcher.group(3)), Integer.parseInt(matcher.group(4))));
                signal.setDateTime(DateUtils.dateTimeParser(matcher.group(5)));
            }
            return signal;
        } catch (IOException e) {
            return new Signal();
        }
    }

    private static Signal parseInitialSignalByStr(String str) {
        Pattern pattern = Pattern.compile(INITIAL_SIGNAL_PATTERN);
        Matcher matcher;
        Signal signal;
        matcher = pattern.matcher(str);
        signal = new Signal();
        if (matcher.find()) {
            signal.setPlaneId(matcher.group(1));
            signal.setPreCoordinate(new Coordinate(Integer.parseInt(matcher.group(2)),
                    Integer.parseInt(matcher.group(3)), Integer.parseInt(matcher.group(4))));
            signal.setDateTime(DateUtils.dateTimeParser(matcher.group(5)));
        }
        return signal;
    }

    private static List<Signal> parseMovementSignals(String filePath) throws IOException {
        Stream<String> stringStream = Files.lines(Paths.get(filePath));
        return stringStream.skip(1).map(str -> {
            return parseMovementSignal(str);
        }).collect(Collectors.toList());

    }

    private static Signal parseMovementSignal(String str) {
        Pattern pattern = Pattern.compile(MOVEMENT_SIGNAL_PATTERN);
        Matcher matcher = pattern.matcher(str);
        Signal signal = new Signal();
        if (matcher.find()) {
            signal.setPlaneId(matcher.group(1));
            signal.setPreCoordinate(new Coordinate(Integer.parseInt(matcher.group(2)),
                    Integer.parseInt(matcher.group(3)), Integer.parseInt(matcher.group(4))));
            signal.setOffset(new Offset(Integer.parseInt(matcher.group(5)), Integer.parseInt(matcher.group(6)),
                    Integer.parseInt(matcher.group(7))));
            signal.setDateTime(DateUtils.dateTimeParser(matcher.group(8)));
        }
        return signal;
    }

    public static HashMap<String, List<Signal>> parseSignalListMap(String filePath) throws IOException {
        HashMap<String, List<Signal>> signalListMap = new HashMap<String, List<Signal>>();
        Stream<String> stringStream = Files.lines(Paths.get(filePath));
        stringStream.forEach(str -> {
            String planeId = parseSignalPlaneId(str);
            if ((planeIdExistInMap(planeId, signalListMap))) {
                signalListMap.get(planeId).add(parseMovementSignal(str));
            } else {
                List<Signal> signalList = new ArrayList<Signal>();
                signalList.add(parseInitialSignalByStr(str));
                signalListMap.put(planeId, signalList);
            }
        });
        return signalListMap;
    }

    private static String parseSignalPlaneId(String str) {
        Pattern pattern = Pattern.compile(SIGNAL_PLANE_PATTERN);
        Matcher matcher = pattern.matcher(str);
        if (matcher.matches()) {
            return matcher.group(1);
        } else {
            return "UNKNOW";
        }
    }

    private static boolean planeIdExistInMap(String planeId, HashMap<String, List<Signal>> signalListMap) {
        return !(signalListMap.get(planeId) == null);
    }

}
