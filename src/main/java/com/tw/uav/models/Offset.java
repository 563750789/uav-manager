package com.tw.uav.models;

public class Offset {
    private int offSetX;
    private int offSetY;
    private int offSetZ;

    public Offset(int offSetX, int offSetY, int offSetZ) {
        this.offSetX = offSetX;
        this.offSetY = offSetY;
        this.offSetZ = offSetZ;
    }

    @Override
    public String toString() {
        return offSetX + " " + offSetY + " " + offSetZ;
    }

    public int getOffSetX() {
        return offSetX;
    }

    public void setOffSetX(int offSetX) {
        this.offSetX = offSetX;
    }

    public int getOffSetY() {
        return offSetY;
    }

    public void setOffSetY(int offSetY) {
        this.offSetY = offSetY;
    }

    public int getOffSetZ() {
        return offSetZ;
    }

    public void setOffSetZ(int offSetZ) {
        this.offSetZ = offSetZ;
    }
}
