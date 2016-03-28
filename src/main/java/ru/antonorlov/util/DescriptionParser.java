package ru.antonorlov.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.antonorlov.entities.Bicycle;
import ru.antonorlov.entities.DirtyBicycle;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by antonorlov on 06/12/14.
 */
public class DescriptionParser {


    static final Logger LOGGER = LogManager.getLogger(DescriptionParser.class.getName());

    private static final String FORK = "вилка";
    private static final String FRAME = "paмa";
    private static final String FRAME2 = "рама";
    private static final String RIMS = "обода";
    private static final String FENDERS = "крылья";
    private static final String BREAKS = "тормоз";
    private static final String SADDLE = "седло";
    private static final String FROND_DERAILLEUR = "FD";
    private static final String REAR_DERAILLEUR = "RD";

    public static Bicycle parseDescription(String dirtyDescription, DirtyBicycle bicycle) throws PriceReaderException {

        Bicycle fullBicycle = new Bicycle(bicycle);

        dirtyDescription = roadBikesLadyGentFix(dirtyDescription);
        String[] characteristics = dirtyDescription.split(",");

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
//        fullBicycle.setRims(setParameter(characteristics, RIMS));
        fullBicycle.setFenders(setParameter(characteristics, FENDERS));
        fullBicycle.setBreaks(setParameter(characteristics, BREAKS));
//        fullBicycle.setSaddle(setParameter(characteristics, SADDLE));

        fullBicycle.setFrontDerailleur(setDerailleur(characteristics, FROND_DERAILLEUR));
        fullBicycle.setRearDerailleur(setDerailleur(characteristics, REAR_DERAILLEUR));

        return fullBicycle;
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
//                        LOGGER.debug(parameter + " [" + derailleurs[x].trim() + ']');
                        return derailleurs[x].trim();
                    }
                }
            }
        }
        return "";
    }

    public static int getNumberOfSpeeds(String[] characteristics) throws PriceReaderException {

        String speedsStr = characteristics[0].trim();
        if(speedsStr.contains("гибрид")){
            speedsStr = characteristics[1].trim();
        }

        Pattern p = Pattern.compile(Constants.PATTERN_SPEEDS_STRING);
        Matcher matcher = p.matcher(speedsStr);
        if (matcher.find()) {
            String str = matcher.group();
            int delimeter = str.indexOf("-");
            str = str.substring(0, delimeter);
            int speeds = Integer.valueOf(str);
            return speeds;
        } else {
            System.out.println("Couldn't parse number of speeds. String[" + speedsStr + ']');
            return 1;
        }
    }


    public static String getShortDescription(Bicycle model) {
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
