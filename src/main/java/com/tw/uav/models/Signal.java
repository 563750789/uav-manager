package com.tw.uav.models;

public class Signal {
    private Coordinate preCoordinate;
    private Offset offset;
    private Boolean invalid;
    private String planeId;

    public Signal() {
        this.invalid = false;
    }

    public Coordinate getPreCoordinate() {
        return preCoordinate;
    }

    public void setPreCoordinate(Coordinate preCoordinate) {
        this.preCoordinate = preCoordinate;
    }

    public Offset getOffset() {
        return offset;
    }

    public void setOffset(Offset offset) {
        this.offset = offset;
    }

    public Boolean isInvalid() {
        return invalid;
    }

    public void setInvalid(Boolean invalid) {
        this.invalid = invalid;
    }

    public String getPlaneId() {
        return planeId;
    }

    public void setPlaneId(String planeId) {
        this.planeId = planeId;
    }

    public Coordinate calculateCurrCoordinate() {
        if (this.invalid || this.offset == null) {
            return this.preCoordinate;
        } else {
            return new Coordinate(this.preCoordinate.getX() + this.offset.getOffSetX(),
                    this.preCoordinate.getY() + this.offset.getOffSetY(), this.preCoordinate.getZ() + this.offset.getOffSetZ());
        }
    }
}
