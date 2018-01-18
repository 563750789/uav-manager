package com.tw.uav.models;


import java.util.ArrayList;
import java.util.List;

public class Plane {
    private String id;
    private Boolean atFault;
    private List<Signal> signalList;

    public Plane() {
        this.signalList = new ArrayList<Signal>();
        this.atFault = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean isAtFault() {
        return atFault;
    }

    public void setAtFault(Boolean atFault) {
        this.atFault = atFault;
    }

    public List<Signal> getSignalList() {
        return signalList;
    }

    public void setSignalList(List<Signal> signalList) {
        this.signalList = signalList;
    }

    public void addSignal(Signal initialSignal) {
        signalList.add(initialSignal);
    }

    public Coordinate getCurrCoordinate() {
        return atFault ? null : getLatestSignal().calculateCurrCoordinate();
    }

    private Signal getLatestSignal() {
        return signalList.get(signalList.size() - 1);
    }
}
