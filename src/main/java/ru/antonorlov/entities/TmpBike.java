package ru.antonorlov.entities;

/**
 * Created by antonorlov on 12/01/15.
 */
public  class TmpBike {

    String dirtyModel;
    String type;
    String modelNum;
    WheelSize wheelSize;
    String year;

    boolean isGirl;
    boolean isBoy;

    boolean isDisc;

    String comment;

    public boolean isDisc() {
        return isDisc;
    }

    public void setDisc(boolean isDisc) {
        this.isDisc = isDisc;
    }

    public String getDirtyModel() {
        return dirtyModel;
    }

    public void setDirtyModel(String dirtyModel) {
        this.dirtyModel = dirtyModel;
    }

    public boolean isGirl() {
        return isGirl;
    }

    public void setGirl(boolean isGirl) {
        this.isGirl = isGirl;
    }

    public boolean isBoy() {
        return isBoy;
    }

    public void setBoy(boolean isBoy) {
        this.isBoy = isBoy;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getModelNum() {
        return modelNum;
    }

    public void setModelNum(String modelNum) {
        this.modelNum = modelNum;
    }

    public WheelSize getWheelSize() {
        return wheelSize;
    }

    public void setWheelSize(WheelSize wheelSize) {
        this.wheelSize = wheelSize;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "TmpBike{" +
                "dirtyModel='" + dirtyModel + '\'' +
                ", type='" + type + '\'' +
                ", modelNum='" + modelNum + '\'' +
                ", wheelSize=" + wheelSize +
                ", year='" + year + '\'' +
                ", isGirl=" + isGirl +
                ", isBoy=" + isBoy +
                ", isDisc=" + isDisc +
                ", comment='" + comment + '\'' +
                '}';
    }
}
