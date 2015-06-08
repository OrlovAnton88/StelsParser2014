package excelnew;

import csv.BaseCSV;
import entities.Bicycle;
import entities.PriceRow;
import entities.TmpBike;
import entities.WheelSize;
import excel.PriceReaderException;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by antonorlov on 11/01/15.
 */
public class PriceParser {


    static final Logger LOGGER = LogManager.getLogger(PriceParser.class.getName());
    private static PriceParser instance = null;

    private String priceFileName;
    private String knownModelsCsvFileName;
    List<String> knownModelsStr;

    private boolean init;

    private int rowToStart = 4;

    private int modelNameColumnNum = 1;
    private int descriptionColumnNum = 2;
    private int priceColumnNum = 6;


    private static final String PILOT = "Pilot";
    private static final String NAVIGATOR = "Navigator";
    private static final String MISS = "Miss";
    public static final String PATTERN_WHEEL_SIZE = "\\d{2}\\\"";
    public static final String PATTERN_MODEL_NUM = "-\\d{3}";
    public static final String PATTERN_MISS_MODEL_NUM = "-\\d{4}";
    public static final String PATTERN_YEAR = "\\.\\d{2}";

    private PriceParser(String priceFileName,
                        String knownModelsCsvFileName) {
        this.priceFileName = priceFileName;
        this.knownModelsCsvFileName = knownModelsCsvFileName;


        knownModelsStr = getKnownModelList(knownModelsCsvFileName);

    }


    public List<Bicycle> getBicycles() throws PriceReaderException {
        if (!init) {
            throw new IllegalStateException("No sheet params specified!");
        }

        HSSFSheet sheet = getSheet(priceFileName);
        List<PriceRow> rows = getDirtyModels(sheet);
List<TmpBike> tmpBikes  = new ArrayList<TmpBike>();
        for (PriceRow row : rows) {
            TmpBike tmpBike = defineBike(row);
            if (tmpBike != null && tmpBike.getType() == null) {
                tmpBike = compareToKnownModels(tmpBike);
            }
            tmpBikes.add(tmpBike);

        }

        BaseCSV baseCSV = new BaseCSV();
        baseCSV.write(tmpBikes);


        List<Bicycle> toReturn = new ArrayList<Bicycle>();

        return toReturn;
    }

    public TmpBike compareToKnownModels(TmpBike undefinedBike) {

        if (undefinedBike.getDirtyModel().contains(".14")) {
            return null;
        }

        for (String knownModel : knownModelsStr) {
            String dirtyModel = undefinedBike.getDirtyModel();
            String[] params = knownModel.split(";");
            String modelName = params[0];
            boolean isDisc = false;
            if (modelName.contains("MD")) {
                isDisc = true;
            }
            String wheel = params[1];

//            List<String> modelNameParts;
//            if (modelName.contains("//s")) {
//                modelNameParts = Arrays.asList(modelName.split("//s"));
//            } else {
//                modelNameParts = new ArrayList<String>();
//                modelNameParts.add(modelName);
//            }
//
//            boolean match = false;
//
//            for (String part : modelNameParts) {
//                if (modelName.toLowerCase().contains(part.toLowerCase())) {
//                    match = true;
//                } else {
//                    match = false;
//                    break;
//                }
//            }


            if (dirtyModel.toLowerCase().contains(modelName.toLowerCase()) && dirtyModel.contains(wheel)) {

                if (isDisc && dirtyModel.contains("MD")) {
                    undefinedBike.setDisc(true);
                }
                undefinedBike.setModelNum(modelName);
                WheelSize ws = WheelSize.getSizeByValue(wheel);
                undefinedBike.setWheelSize(ws);
                return undefinedBike;
            }
        }
        return undefinedBike;
    }


    public TmpBike defineBike(PriceRow row) {

        if (row.getModelName() != null) {
            TmpBike tmpBike = new TmpBike();
            String dirtyModel = row.getModelName();

            //remove whitespaces
            while (dirtyModel.contains("  ")) {
                dirtyModel = dirtyModel.replace("  ", " ");
            }

            tmpBike.setDirtyModel(dirtyModel);

//            System.out.println("Processing[" +dirtyModel + "]");

            //define wheel size
            Pattern wheelPattern = Pattern.compile(PATTERN_WHEEL_SIZE);
            Matcher wheelMatcher = wheelPattern.matcher(dirtyModel);
            if (wheelMatcher.find()) {
                String wheelSizeStr = wheelMatcher.group(0);
                if (wheelSizeStr != null) {
                    WheelSize ws = WheelSize.getSizeByValue(wheelSizeStr.substring(0, wheelSizeStr.length() - 1));
                    tmpBike.setWheelSize(ws);
                } else {
                    System.out.println("WheelSize is null for " + dirtyModel);
                }

                //remove wheel info from string
                dirtyModel = dirtyModel.replaceAll(wheelSizeStr, "");
            } else {
//                System.out.println("NO MATCH OF WHEELS FOR " + row.getModelName());
            }

            //do not cover MISS models
            if (!dirtyModel.toLowerCase().contains("miss")) {
                Pattern modelPattern = Pattern.compile(PATTERN_MODEL_NUM);
                Matcher modelNumMatcher = modelPattern.matcher(dirtyModel);
                if (modelNumMatcher.find()) {
                    String modelNum = modelNumMatcher.group(0);


                    dirtyModel = dirtyModel.replaceAll(modelNum, "");

                    //remove dash
                    modelNum = modelNum.substring(1, modelNum.length());
                    tmpBike.setModelNum(modelNum);
                } else {
//                System.out.println("NO MATCH OF MODEL NUM FOR " + row.getModelName());
                }
            }
            if (dirtyModel.contains("Girl") || dirtyModel.contains("Lady")) {
                tmpBike.setGirl(true);
                dirtyModel = dirtyModel.replaceAll("Girl", "");
                dirtyModel = dirtyModel.replaceAll("Lady", "");

            }

            if (dirtyModel.contains("Boy") || dirtyModel.contains("Gent")) {
                dirtyModel = dirtyModel.replaceAll("Boy", "");
                dirtyModel = dirtyModel.replaceAll("Gent", "");
                tmpBike.setBoy(true);
            }

            Pattern yearPattern = Pattern.compile(PATTERN_YEAR);
            Matcher yearMatcher = yearPattern.matcher(dirtyModel);
            if (yearMatcher.find()) {
                String year = yearMatcher.group(0);
                if (".15".equals(year)) {
                    tmpBike.setYear("2015");
                    dirtyModel = dirtyModel.replaceAll("\\.15", "");
                } else if (".14".equals(year)) {
                    tmpBike.setYear("2014");
                    dirtyModel = dirtyModel.replaceAll("\\.14", "");
                } else {
                    return null;
//                        System.out.println("BEFORE 2014");
                }
            } else {
                tmpBike.setYear("2015");
            }


            if (dirtyModel.contains("27.5\"")) {
                tmpBike.setWheelSize(WheelSize.TWENTYSEVENANDHALF);
                dirtyModel = dirtyModel.replaceAll("27\\.5\\\"", "");
            }

            if (dirtyModel.contains("MD")) {
                tmpBike.setDisc(true);
                dirtyModel = dirtyModel.replaceAll("MD", "");
            }

            if (dirtyModel.contains("Disc")) {
                tmpBike.setDisc(true);
                dirtyModel = dirtyModel.replaceAll("Disc", "");
            }


            if (dirtyModel.contains("(новый дизайн)")) {
                tmpBike.setComment("(новый дизайн)");
                dirtyModel = dirtyModel.replaceAll("\\(новый дизайн\\)", "");
            }


            if (dirtyModel.contains("(новая модель)")) {
                tmpBike.setComment("(новая модель)");
                dirtyModel = dirtyModel.replaceAll("\\(новая модель\\)", "");
            }


            //process Pilots
            if (dirtyModel.contains("Pilot")) {

                tmpBike.setType(PILOT);
                dirtyModel = dirtyModel.replaceAll(PILOT, "");

//                System.out.println(tmpBike);


            } else if (dirtyModel.contains("Navigator")) {
                tmpBike.setType(NAVIGATOR);
                dirtyModel = dirtyModel.replaceAll(NAVIGATOR, "");


                if (dirtyModel.contains("D")) {
                    tmpBike.setDisc(true);
                    dirtyModel = dirtyModel.replaceAll("D", "");
                }


                //V-break
                if (dirtyModel.contains("V")) {
                    tmpBike.setDisc(false);
                    dirtyModel = dirtyModel.replaceAll("V", "");
                }


            } else if (dirtyModel.contains(MISS)) {
                tmpBike.setType(MISS);
                Pattern missModelPattern = Pattern.compile(PATTERN_MISS_MODEL_NUM);
                Matcher missModelNumMatcher = missModelPattern.matcher(dirtyModel);
                if (missModelNumMatcher.find()) {
                    String modelNum = missModelNumMatcher.group(0);


                    dirtyModel = dirtyModel.replaceAll(modelNum, "");

                    //remove dash
                    modelNum = modelNum.substring(1, modelNum.length());
                    tmpBike.setModelNum(modelNum);
                }
//                System.out.println("DIRTY MODEL: " + dirtyModel);

            }
            return tmpBike;
        }
        return null;

    }

    public PriceParser setSheetParams(int rowToStart,
                                      int modelNameColumnNum,
                                      int descriptionColumnNum,
                                      int priceColumnNum) {
        this.rowToStart = rowToStart;
        this.modelNameColumnNum = modelNameColumnNum;
        this.descriptionColumnNum = descriptionColumnNum;
        this.priceColumnNum = priceColumnNum;

        init = true;
        return this;
    }

    /**
     * Get sheet from file to read
     *
     * @param fileName
     * @return
     * @throws PriceReaderException
     */
    private HSSFSheet getSheet(String fileName) throws PriceReaderException {
        HSSFSheet sheet;
        try {
            InputStream inputStream = new FileInputStream(fileName);
            HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
            sheet = workbook.getSheetAt(0);       // first sheet
        } catch (FileNotFoundException e) {
            throw new PriceReaderException("File not found: " + e);
        } catch (IOException e) {
            throw new PriceReaderException("Wrong file format: " + e);
        }
        return sheet;
    }

    private List<PriceRow> getDirtyModels(HSSFSheet sheet) {

        List<PriceRow> list = new ArrayList<PriceRow>(200);

        int numOfRows = sheet.getPhysicalNumberOfRows();

        for (int i = rowToStart; i < numOfRows; i++) {
            PriceRow priceRow = new PriceRow();

            Row row = sheet.getRow(i);
            if (row == null) {
                break;
            }
            Cell dirtyModelName = row.getCell(modelNameColumnNum);
            Cell description = row.getCell(descriptionColumnNum);
            Cell price = row.getCell(priceColumnNum);
            if (Cell.CELL_TYPE_STRING == dirtyModelName.getCellType()) {
                String cellValue = dirtyModelName.getStringCellValue();
                priceRow.setModelName(cellValue);
            } else {
                continue;
            }

            if (Cell.CELL_TYPE_STRING == description.getCellType()) {
                String cellValue = description.getStringCellValue();
                priceRow.setDescription(cellValue);
            } else {
                continue;
            }

            if (Cell.CELL_TYPE_NUMERIC == price.getCellType()) {
                Double cellValue = price.getNumericCellValue();
                priceRow.setRetailPrice(cellValue);
            } else {
                continue;
            }

            list.add(priceRow);
        }
        return list;

    }

    private List<String> getKnownModelList(String filename) {
        List<String> lines = null;
        try {
            File file = new File(filename);
            lines = FileUtils.readLines(file, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return lines;

    }


    public static PriceParser getInstance(String priceFileName,
                                          String knownModelsCsvFileName) {
        if (instance == null) {
            instance = new PriceParser(priceFileName, knownModelsCsvFileName);
        }
        return instance;
    }



}
