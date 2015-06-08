package excel;

import net.ricecode.similarity.*;
import org.apache.commons.io.FileUtils;
import uk.ac.shef.wit.simmetrics.similaritymetrics.AbstractStringMetric;
import uk.ac.shef.wit.simmetrics.similaritymetrics.MongeElkan;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by antonorlov on 01/12/14.
 */
public class ModelComporator {

    static String one = "Pilot-120.";
    static String two = "Pilot 110";
    static String three = "Pilot 120";

    public static void main(String[] args) {

        List<String> list = getModels();

        SimilarityStrategy strategy = new LevenshteinDistanceStrategy();
        String target = "McDonalds";
        String source = "MacMahons";
        StringSimilarityService service = new StringSimilarityServiceImpl(strategy);

//        double score = service.score(source, target); // Score is 0.90


        for (String model : list) {
//            model = model.replace("Stels","");
            double score = service.score(two, model.trim());
//            metric.get
            if (score > 0.55) {
                System.out.println(model + " - " + score);
            }

        }


    }


    private static List<String> getModels() {
        List<String> lines = null;
        try {
            File file = new File("/Users/antonorlov/Documents/StelsParser2014/src/main/resources/model_list.csv");
            lines = FileUtils.readLines(file, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return lines;

    }


    public static String preProcessModel(String model) {
        String toReturn = model;
        if(!toReturn.contains("27.5"))
        toReturn = toReturn.replaceAll("\\.", " ");

        if (!toReturn.contains("14\"")) {
            toReturn = toReturn.replaceAll("\\.14", " ");
        }

            toReturn = toReturn.replaceAll("\\.15", " ");
            toReturn = toReturn.replaceAll(" 15 ", " ");
            toReturn = toReturn.replaceAll(". 15", " ");

            toReturn = toReturn.replaceAll("MD", "Disc");
            toReturn = toReturn.replaceAll(" V ", " ");

            toReturn = toReturn.replaceAll("(новый дизайн)", " ");
            toReturn = toReturn.replaceAll("(новая модель)", " ");


        while (toReturn.contains("  ")){
            toReturn = toReturn.replace("  "," ");
        }

        return toReturn;
    }

}