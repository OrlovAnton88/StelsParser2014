package excel;

import entities.FullBicycle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.Constants;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: Mouse
 * Date: 25.02.14
 * Time: 23:20
 * To change this template use File | Settings | File Templates.
 */
public class FileReadeHelper {

    private static final String FORK = "вилка";
    private static final String FRAME = "paмa";
    private static final String FRAME2 = "рама";
    private static final String RIMS = "обода";
    private static final String FENDERS = "крылья";
    private static final String BREAKS = "тормоз";
    private static final String SADDLE = "седло";
    private static final String FROND_DERAILLEUR = "FD";
    private static final String REAR_DERAILLEUR = "RD";


    static final Logger LOGGER = LogManager.getLogger(FileReadeHelper.class);

    public static void parseDescription(String cellValue, FullBicycle fullBicycle) throws PriceReaderException {
        cellValue = roadBikesLadyGentFix(cellValue);
        String[] characteristics = cellValue.split(",");

        fullBicycle.setSpeedsNum(getNumberOfSpeeds(characteristics));
        String frame = null;
        if ((frame = setParameter(characteristics, FRAME)) == null) {
            frame = setParameter(characteristics, FRAME2);
        }
        if (frame != null) {
            frame = frame.replaceAll("рама", "").trim();
            frame = frame.replaceAll("paмa", "").trim();
        }
        fullBicycle.setFrame(frame);
        fullBicycle.setFrontFork(setParameter(characteristics, FORK));
        fullBicycle.setRims(setParameter(characteristics, RIMS));
        fullBicycle.setFenders(setParameter(characteristics, FENDERS));
        fullBicycle.setBreaks(setParameter(characteristics, BREAKS));
        fullBicycle.setSaddle(setParameter(characteristics, SADDLE));

        fullBicycle.setFrontDerailleur(setDerailleur(characteristics, FROND_DERAILLEUR));
        fullBicycle.setRearDerailleur(setDerailleur(characteristics, REAR_DERAILLEUR));
    }

    private static String roadBikesLadyGentFix(String str) {
        int start = 0;
        int end = 0;
        if ((start = str.indexOf('(')) > 0 && (end = str.indexOf(")")) > 0 && str.substring(start, end).indexOf(',') > 0) {
            String begining = str.substring(0, start);
            String ending = str.substring(end, str.length());
            String middle = str.substring(start, end);
            if (middle.contains("Lady")) {
                middle = middle.replace(",", " ");
                return begining + middle + ending;
            } else {
                return str;
            }

        } else {
            return str;
        }
    }

    public static void parsePrice(double cellValue, FullBicycle fullBicycle) throws PriceReaderException {
        int price = (int) Math.round(cellValue);
        fullBicycle.setPrice(price);
    }


    private static String setParameter(String str[], String parameter) {
        for (int i = 1; i < str.length; i++) {
            if (str[i].contains(parameter)) {
//                LOGGER.debug(parameter + " [" + str[i].trim() + ']');
                return str[i].trim();
            }
        }
        return null;
    }


    private static String setDerailleur(String[] str, String parameter) {
        for (int i = 0; i < str.length; i++) {
            if (str[i].contains(parameter)) {
                String derailleurs[] = str[i].split("/");

                for (int x = 0; x < derailleurs.length; x++) {
                    if (derailleurs[x].contains(parameter)) {
                        LOGGER.debug(parameter + " [" + derailleurs[x].trim() + ']');
                        return derailleurs[x].trim();
                    }
                }
            }
        }
        return "";
    }

    public static int getNumberOfSpeeds(String[] characteristics) throws PriceReaderException {
        String speedsStr = characteristics[0].trim();
        Pattern p = Pattern.compile(Constants.PATTERN_SPEEDS_STRING);
        Matcher matcher = p.matcher(speedsStr);
        if (matcher.find()) {
            String str = matcher.group();
            int delimeter = str.indexOf("-");
            str = str.substring(0, delimeter);
            int speeds = Integer.valueOf(str);
            return speeds;
        } else {
            throw new PriceReaderException("Couldn't parse number of speeds. String[" + speedsStr + ']');
        }
    }

    public static String reduceSpaces(String stringIn) {
        return stringIn.replaceAll("\\s+", " ");
    }

    public static void processCrossModel(String model, FullBicycle bicicle) {
        bicicle.setModel(model);
        bicicle.setWheelsSize("28");
    }

    public static void process175Model(String model, FullBicycle fullBicycle) {
        String wheelSize = model.substring(0, 4);
        String modelName = model.substring(5, model.length()).trim();
        fullBicycle.setWheelsSize(wheelSize);
        fullBicycle.setModel(modelName);
    }

    public static boolean isCrossModel(String model) {
        if (model.contains("Cross")) {
            return true;
        }
        return false;
    }

    public static boolean is275Model(String model) {
        if (model.contains("27,5")) {
            return true;
        }
        return false;
    }


    /**
     * it's assumed if model has new design, then model with old desing is 2013 model
     *
     * @param fullBicycles
     */
    public static void removeOldModels(Collection<FullBicycle> fullBicycles) {
        Collection modelNamesToRemove = new ArrayList();
        for (FullBicycle fullBicycle : fullBicycles) {
            if (fullBicycle.getModel().contains("(новый дизайн)")) {
                modelNamesToRemove.add(fullBicycle.getModel().replace("(новый дизайн)", "").toLowerCase().trim());
            }
        }
        Iterator<FullBicycle> iterator = fullBicycles.iterator();
        while (iterator.hasNext()) {
            FullBicycle fullBicycle = iterator.next();
            if (modelNamesToRemove.contains(fullBicycle.getModel().toLowerCase())) {
                iterator.remove();
                LOGGER.debug("Model [" + fullBicycle.getModel() + "] removed");
            }
        }

    }

    public static void generateProdCode(FullBicycle fullBicycle) throws PriceReaderException {
        String model = fullBicycle.getModel();
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
        if (model.indexOf('(') > 0) {
            model = model.replaceAll("\\(|\\)", "_").trim();
        }
        model = model.replaceAll("\\s", "").trim();
        result.append(model);
        result.append(fullBicycle.getWheelsSize());
        try {
            result.append('_');
            result.append(Constants.YEAR);
        } catch (StringIndexOutOfBoundsException ex) {
            throw new PriceReaderException("Error generating product code for model [" + model + ']' + ex);
        }
        fullBicycle.setProductCode(result.toString());
    }


    public static void addLadyModelsToRoadBikes(Collection<FullBicycle> fullBicycles) throws PriceReaderException {
        Collection<FullBicycle> newBikes = new ArrayList<FullBicycle>();
        Iterator<FullBicycle> iterator = fullBicycles.iterator();
        while (iterator.hasNext()) {
            FullBicycle fullBicycle = iterator.next();
            String frame = fullBicycle.getFrame();
            if (frame != null && frame.indexOf("(Gent") > 0 && frame.indexOf("Lady") > 0) {
                FullBicycle newBicycle = null;
                try {
                    newBicycle = (FullBicycle) fullBicycle.clone();
                    newBicycle.setModel(fullBicycle.getModel() + " Lady");
                } catch (CloneNotSupportedException ex) {
                    LOGGER.error(ex);
                }
                FileReadeHelper.generateProdCode(newBicycle);
                newBikes.add(newBicycle);
            }

        }
        fullBicycles.addAll(newBikes);
    }

    public static void addWheelSizeToModelWithIdenticalNames(Collection<FullBicycle> fullBicycles) {
        Iterator<FullBicycle> iterator = fullBicycles.iterator();
        Set<String> uniqueModes = new HashSet<String>();
        Set<String> sameModelName = new HashSet<String>();
        while (iterator.hasNext()) {
            FullBicycle fullBicycle = iterator.next();
            String modelName = fullBicycle.getModel();
            boolean unique = uniqueModes.add(modelName);
            if (!unique) {
                sameModelName.add(modelName);
            }
        }
        Iterator<FullBicycle> secondIterator = fullBicycles.iterator();
        while (secondIterator.hasNext()) {
            FullBicycle fullBicycle = secondIterator.next();
            String modelName = fullBicycle.getModel();
            if (sameModelName.contains(modelName)) {
                fullBicycle.setModel(modelName + " " + fullBicycle.getWheelsSize());
            }
        }
    }

    public static void addWheelSizeToSomeKidsModels(Collection<FullBicycle> fullBicycles){
        Iterator<FullBicycle> iterator = fullBicycles.iterator();
        Set <String> kidsModelsToAddWheelsSize = getModelsToAddwheelSize();
        while (iterator.hasNext()) {
            FullBicycle fullBicycle = iterator.next();
            if(kidsModelsToAddWheelsSize.contains(fullBicycle.getModel())){
                fullBicycle.setModel(fullBicycle.getModel() + " " + fullBicycle.getWheelsSize());
            }

        }

    }
    private static Set<String> getModelsToAddwheelSize(){
        Set<String> set = new HashSet<String>();
        set.add("Fortune");
        set.add("Talisman (chrome)");
        set.add("Pilot 180 (новый дизайн)");
        set.add("Pilot 120");
        return set;
    }

    public static void generateShortDescription(Collection<FullBicycle> fullBicycles) {
        Collection modelNamesToRemove = new ArrayList();
        for (FullBicycle fullBicycle : fullBicycles) {
            fullBicycle.setShortDescription(getShortDescription(fullBicycle));
        }
    }

    public static String getShortDescription(FullBicycle model) {
        String strMod = model.getModel();
        StringBuffer result = new StringBuffer();
        try {
            Pattern p = Pattern.compile("\\d\\d\\d");
            //  get a matcher object
            Matcher m = p.matcher(strMod);
            String tmp = "";
            if (m.find()) {
                tmp = m.group();
            }
            int modNum = Integer.parseInt(tmp);
            if (modNum >= 500 && strMod.contains("Navigator")) {
                result.append("Горный велосипед ");
            } else if (modNum >= 200 && modNum < 390 && strMod.contains("Navigator")) {
                if (strMod.contains("Lady")) {
                    result.append("Женский дорожный велосипед ");
                } else {
                    result.append("Дорожный велосипед ");
                }
            } else if (strMod.contains("Cross") || (strMod.contains("Navigator") && modNum == 170)) {
                result.append("Гибридный велосипед ");
            } else if (strMod.contains("Miss")) {
                result.append("Женский горный велосипед ");
            } else if (strMod.contains("Pilot") && (modNum > 300 && modNum < 830)) {
                result.append("Складной велосипед ");
            } else {
                result.append("Велосипед ");
            }
        } catch (NumberFormatException ex) {
            LOGGER.error("Can't define bicycle type: " + ex.getMessage());
            result.append("Велосипед ");

        }
        String wheels = model.getWheelsSize();
        if (wheels.equals("24")) {
            result.append("с колесами " + wheels + " дюйма.");
        } else {
            result.append("с колесами " + wheels + " дюймов.");
        }

        if (strMod.toLowerCase().contains("disc")) {
            int size = result.length();
            result.delete(size - 1, size);
            result.append(" и дисковыми тормозами. ");

        }
        String frameMaterial = model.getFrame();
        if (frameMaterial != null) {
            if (frameMaterial.toLowerCase().contains("ста")) {
                result.append(" Рама: сталь.");
            } else if (frameMaterial.toLowerCase().contains("AL")) {
                result.append(" Рама: алюминий.");
            }
        }
        try {
            int speeds = model.getSpeedsNum();
            result.append(" " + speeds);
            if (speeds == 21 || speeds == 1) {
                result.append(" скорость. ");
            } else if (speeds == 18 || speeds == 5 || speeds == 7 || speeds == 6 || speeds == 12) {
                result.append(" скоростей. ");

            } else {
                result.append(" скорости. ");
            }
        } catch (NumberFormatException ex) {
            System.out.println("Cant parse speeds number: " + ex.getMessage());
        }

        return result.toString();
    }


}
