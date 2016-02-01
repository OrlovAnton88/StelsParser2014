package ru.antonorlov.util;

import ru.antonorlov.entities.FullBicycle;

import java.util.List;

/**
 * Created by antonorlov on 29/01/16.
 */
public class Util {


    public static void transformTo2016(List<FullBicycle> list){

        for (FullBicycle fullBicycle : list) {
            String productCode = fullBicycle.getProductCode();
            String newProductCode = productCode.substring(0, productCode.length() - 2) + "16";
            fullBicycle.setProductCode(newProductCode);
            //изменяем заголовок
            fullBicycle.setModel(fullBicycle.getModel().replaceAll("2015", "2016"));
            //change image name
            fullBicycle.setImageName(fullBicycle.getImageName().replaceAll("15.jpg","16.jpg"));

            fullBicycle.setTransformed(true);
        }
    }
}
