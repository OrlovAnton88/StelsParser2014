package ru.antonorlov.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by antonorlov on 29/03/16.
 */
@Component
public class PriceConfig {

    @Value("#{T(java.lang.Integer).parseInt('${stels.row_to_start}')}")
    private int stelsRowToStart;
    @Value("#{T(java.lang.Integer).parseInt('${stels.model_name_column_number}')}")
    private int stelsModelNameColumnNum;
    @Value("#{T(java.lang.Integer).parseInt('${stels.description_column_number}')}")
    private int stelsDescriptionColumnNum;
    @Value("#{T(java.lang.Integer).parseInt('${stels.price_column_number}')}")
    private int stelsPriceColumnNum;

    @Value("#{T(java.lang.Integer).parseInt('${forward.row_to_start}')}")
    private int forwardRowToStart;
    @Value("#{T(java.lang.Integer).parseInt('${forward.model_name_column_number}')}")
    private int forwardModelNameColumnNum;
    @Value("#{T(java.lang.Integer).parseInt('${forward.description_column_number}')}")
    private int forwardDescriptionColumnNum;
    @Value("#{T(java.lang.Integer).parseInt('${forward.price_column_number}')}")
    private int forwardPriceColumnNum;

    public int getRowToStart(Brand brand){
        if(brand.equals(Brand.STELS)){
            return stelsRowToStart;
        }else if(brand.equals(Brand.FORWARD)){
            return forwardRowToStart;
        }else {
            throw new IllegalArgumentException("Unknown brand[" + brand.name() + "]");
        }
    }

    public int getModelNameColumn(Brand brand){
        if(brand.equals(Brand.STELS)){
            return stelsModelNameColumnNum;
        }else if(brand.equals(Brand.FORWARD)){
            return forwardModelNameColumnNum;
        }else {
            throw new IllegalArgumentException("Unknown brand[" + brand.name() + "]");
        }
    }

    public int getPriceColumn(Brand brand){
        if(brand.equals(Brand.STELS)){
            return stelsDescriptionColumnNum;
        }else if(brand.equals(Brand.FORWARD)){
            return forwardPriceColumnNum;
        }else {
            throw new IllegalArgumentException("Unknown brand[" + brand.name() + "]");
        }
    }

    public int getDescriptionColumnNum(Brand brand){
        if(brand.equals(Brand.STELS)){
            return stelsDescriptionColumnNum;
        }else if(brand.equals(Brand.FORWARD)){
            return forwardPriceColumnNum;
        }else {
            throw new IllegalArgumentException("Unknown brand[" + brand.name() + "]");
        }
    }


}
