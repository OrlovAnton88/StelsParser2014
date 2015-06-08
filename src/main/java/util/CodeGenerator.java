package util;

import entities.Bicycle;
import excel.PriceReaderException;

/**
 * Created by antonorlov on 06/12/14.
 */
public class CodeGenerator {

    public static String generateCode(Bicycle bicycle) throws PriceReaderException{

        String model = bicycle.getModel();
        if (model == null) {
            throw new PriceReaderException("model name is null");
        }
        StringBuffer result = new StringBuffer("s");
        model = model.toLowerCase();
        model = model.replace("(новая модель)", "");
        model = model.replace("(новый дизайн)", "");
        model = model.replace("cross", "");
        model = model.replace("pilot", "p");
        model = model.replace("miss", "m");
        model = model.replace("navigator", "n");
        model = model.replaceAll("girl","g");
        model = model.replaceAll("boy","b");
        model = model.replaceAll("lady","l");
        model = model.replaceAll("gent","g");
        model = model.replaceAll("ск","sp");
        model = model.replaceAll("-","");
        model = model.replaceAll(",","");
        model = model.replace("2015","15");
        if (model.indexOf('(') > 0) {
            model = model.replaceAll("\\(|\\)", "_").trim();
        }
        model = model.replaceAll("\\s", "").trim();
        model =    model.replaceAll("\"","").trim();
        result.append(model);



//        String wheelSize = bicycle.getWheelSize().getSize();
//        result.append(wheelSize.substring(0,wheelSize.length()-1));

//        try {
//            result.append('_');
//            result.append(bicycle.getYear());
//        } catch (StringIndexOutOfBoundsException ex) {
//            throw new PriceReaderException("Error generating product code for model [" + model + ']' + ex);
//        }
        return result.toString();

    }

}
