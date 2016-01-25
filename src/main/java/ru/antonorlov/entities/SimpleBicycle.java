package ru.antonorlov.entities;

/**
 * Created by antonorlov on 06/12/14.
 */
public class SimpleBicycle {

    private String model;

    private WheelSize wheelSize;

    private String dirtyModel;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public WheelSize getWheelSize() {
        return wheelSize;
    }

    public void setWheelSize(WheelSize wheelSize) {
        this.wheelSize = wheelSize;
    }

    public String getDirtyModel() {
        return dirtyModel;
    }

    public void setDirtyModel(String dirtyModel) {
        this.dirtyModel = dirtyModel;
    }

    @Override
    public String toString() {
        return "SimpleBicycle{" +
                "model='" + model + '\'' +
                ", wheelSize=" + wheelSize +
                '}';
    }
}
