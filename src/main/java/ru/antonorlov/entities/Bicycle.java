package ru.antonorlov.entities;

import ru.antonorlov.util.Brand;
import ru.antonorlov.util.Year;

import javax.persistence.*;

@Entity
public class Bicycle {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

//    /**
//     * Идентификатор прайс-листа к которому привязан велосипед
//     */
//    @ManyToOne
//    private Integer priceId;

    @Column(nullable = false)
    private String productCode;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private int price;

    @Column
    @Enumerated(value = EnumType.STRING)
    private Year year;

    @Column
    @Enumerated(value = EnumType.STRING)
    private Brand brand;

    @Column
    private String wheelsSize;

    @Column
    private String imageName;
    @Column
    private String imageName2;
    @Column
    private String imageName3;

    @Column
    private String frameSize;

    @Column
    private String frame;

    @Column
    private int speedsNum;

    @Column
    private String frontFork;
    @Column
    private String frontDerailleur;
    @Column
    private String rearDerailleur;

    /*Крылья*/
    @Column
    private String fenders;

    @Column
    private String breaks;


    @Column
    private String shortDescription;

    /**
     * true if model not from price list, but constructed syntatically
     */
    @Column
    private boolean transformed = false;

//
//    private String trademark;
//    public String url;
//    public Integer categoryId;
//    private String colors;
//    private String steeringTube;
//    /* Каретка */
//    private String bottomBracket;
//    /* шатуны */
//    private String crankset;
//    private String rearHub;
//    private String frontHub;
//    /*Трешетка*/
//    private String freeWheel;
//    private String shifters;
//    /*обода*/
//    private String rims;
//    private String tyres;
//    private String pedals;
//    private String saddle;
//    /*багажник*/
//    private String rack;

    public Bicycle() {
    }

    public Bicycle(DirtyBicycle bicycle) {
        setModel(bicycle.getModel());
        setPrice(bicycle.getPrice());
        setProductCode(bicycle.getProdCode());
        setWheelsSize(bicycle.getWheelSize().getSize());
    }


    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }


    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }


    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getImageName3() {
        return imageName3;
    }

    public void setImageName3(String imageName3) {
        this.imageName3 = imageName3;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageName2() {
        return imageName2;
    }

    public void setImageName2(String imageName2) {
        this.imageName2 = imageName2;
    }


    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getWheelsSize() {
        return wheelsSize;
    }

    public void setWheelsSize(String wheelsSize) {
        this.wheelsSize = wheelsSize;
    }

    public String getFrameSize() {
        return frameSize;
    }

    public void setFrameSize(String frameSize) {
        this.frameSize = frameSize;
    }

    public String getFrame() {
        return frame;
    }

    public void setFrame(String frame) {
        this.frame = frame;
    }


    public boolean isTransformed() {
        return transformed;
    }

    public void setTransformed(boolean transformed) {
        this.transformed = transformed;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Year getYear() {
        return year;
    }

    public void setYear(Year year) {
        this.year = year;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public int getSpeedsNum() {
        return speedsNum;
    }

    public void setSpeedsNum(int speedsNum) {
        this.speedsNum = speedsNum;
    }

    public String getFrontFork() {
        return frontFork;
    }

    public void setFrontFork(String frontFork) {
        this.frontFork = frontFork;
    }

    public String getFrontDerailleur() {
        return frontDerailleur;
    }

    public void setFrontDerailleur(String frontDerailleur) {
        this.frontDerailleur = frontDerailleur;
    }

    public String getRearDerailleur() {
        return rearDerailleur;
    }

    public void setRearDerailleur(String rearDerailleur) {
        this.rearDerailleur = rearDerailleur;
    }

    public String getFenders() {
        return fenders;
    }

    public void setFenders(String fenders) {
        this.fenders = fenders;
    }

    public String getBreaks() {
        return breaks;
    }

    public void setBreaks(String breaks) {
        this.breaks = breaks;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bicycle that = (Bicycle) o;

        if (!productCode.equals(that.productCode)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return productCode.hashCode();
    }
}
