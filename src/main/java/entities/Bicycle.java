package entities;

import java.util.List;

/**
 * Created by antonorlov on 06/12/14.
 */
public class Bicycle {

    private String prodCode;

    private String model;

    private String dirtyModel;

    private String shortDesc;

    private String fullDesc;

    private WheelSize wheelSize;

    private List<String> colors;

    private int year;

    private int price;

    private String description;

    public Bicycle(SimpleBicycle bicycle) {
        this.model = bicycle.getModel();
        this.wheelSize = bicycle.getWheelSize();
    }

    public String getDirtyModel() {
        return dirtyModel;
    }

    public void setDirtyModel(String dirtyModel) {
        this.dirtyModel = dirtyModel;
    }

    public String getProdCode() {
        return prodCode;
    }

    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getFullDesc() {
        return fullDesc;
    }

    public void setFullDesc(String fullDesc) {
        this.fullDesc = fullDesc;
    }

    public WheelSize getWheelSize() {
        return wheelSize;
    }

    public void setWheelSize(WheelSize wheelSize) {
        this.wheelSize = wheelSize;
    }

    public List<String> getColors() {
        return colors;
    }

    public void setColors(List<String> colors) {
        this.colors = colors;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Bicycle{" +
                "prodCode='" + prodCode + '\'' +
                ", model='" + model + '\'' +
                ", shortDesc='" + shortDesc + '\'' +
                ", fullDesc='" + fullDesc + '\'' +
                ", wheelSize=" + wheelSize.getSize() +
                ", colors=" + colors +
                ", year=" + year +
                ", price=" + price +
                ", description='" + description + '\'' +
                '}';
    }
}
