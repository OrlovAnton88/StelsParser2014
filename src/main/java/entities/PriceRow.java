package entities;

/**
 * Created by antonorlov on 02/12/14.
 */
public class PriceRow {
    private String modelName;
    private String description;
    private Double retailPrice;
    private Double price400;
    private Double price1bln;
    private Double price1_5bln;

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(Double retailPrice) {
        this.retailPrice = retailPrice;
    }

    public Double getPrice400() {
        return price400;
    }

    public void setPrice400(Double price400) {
        this.price400 = price400;
    }

    public Double getPrice1bln() {
        return price1bln;
    }

    public void setPrice1bln(Double price1bln) {
        this.price1bln = price1bln;
    }

    public Double getPrice1_5bln() {
        return price1_5bln;
    }

    public void setPrice1_5bln(Double price1_5bln) {
        this.price1_5bln = price1_5bln;
    }
}
