//package ru.antonorlov.excel;
//
//import net.ricecode.similarity.LevenshteinDistanceStrategy;
//import net.ricecode.similarity.SimilarityStrategy;
//import net.ricecode.similarity.StringSimilarityService;
//import net.ricecode.similarity.StringSimilarityServiceImpl;
//import org.apache.commons.io.FileUtils;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.apache.poi.hssf.usermodel.HSSFSheet;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.Row;
//import ru.antonorlov.entities.FullBicycle;
//import ru.antonorlov.entities.PriceRow;
//
//import java.io.*;
//import java.util.ArrayList;
//import java.util.List;
//
//
///**
// * Created by antonorlov on 01/12/14.
// */
//@Deprecated
//public class FileReader {
//
//    static final Logger LOGGER = LogManager.getLogger(FileReader.class.getName());
//    private static final int rowToStart = 4;
//    private static final int modelNameColumnNum = 1;
//    private static final int descriptionColumnNum = 2;
//    private static final int retailPriceColumnNum = 6;
//    private static FileReader instance = null;
//    private List<FullBicycle> bicycles;
//    private List<String> knownModelList;
//
//
//    private FileReader() {
//        bicycles = new ArrayList<FullBicycle>(100);
//        knownModelList = getKnownModelList();
//    }
//
//    public static FileReader getInstance() {
//        if (instance == null) {
//            instance = new FileReader();
//        }
//        return instance;
//    }
//
//    ;
//
//    public List<FullBicycle> getModels() {
//        return bicycles;
//    }
//
//    @Deprecated
//    public void oldreadFile(String filename) throws PriceReaderException {
//        HSSFSheet sheet = getSheet(filename);
//        int numerOfRows = sheet.getPhysicalNumberOfRows();
//
//        for (int i = rowToStart; i < numerOfRows; i++) {
//            FullBicycle fullBicycle = new FullBicycle();
//            Row row = sheet.getRow(i);
//            if (row == null) {
//                break;
//            }
//            Cell dirtyModelName = row.getCell(modelNameColumnNum);
//            Cell description = row.getCell(descriptionColumnNum);
//            Cell retailPrice = row.getCell(retailPriceColumnNum);
//            if (Cell.CELL_TYPE_STRING == dirtyModelName.getCellType()) {
//                String cellValue = dirtyModelName.getStringCellValue();
//                if (cellValue.contains("2014")) {
//                    continue;
//                }
//                parseModelName(cellValue, fullBicycle);
//            } else {
//                throw new PriceReaderException("Name cell is not of string type");
//            }
//            if (Cell.CELL_TYPE_STRING == description.getCellType()) {
//                FileReadeHelper.parseDescription(description.getStringCellValue(), fullBicycle);
//            } else {
//                throw new PriceReaderException("Description cell is not of string type");
//            }
//            if (Cell.CELL_TYPE_NUMERIC == retailPrice.getCellType()) {
//                FileReadeHelper.parsePrice(retailPrice.getNumericCellValue(), fullBicycle);
//            } else {
//                LOGGER.error("Cell cell is not of numeric type. Cell type [" + retailPrice.getCellType() + ']');
//                fullBicycle.setPrice(0);
//            }
//
//            LOGGER.debug("FullBicycle generated: " + fullBicycle.toString());
//            FileReadeHelper.generateProdCode(fullBicycle);
//            bicycles.add(fullBicycle);
//        }
//    }
//
//    @Deprecated
//    private void parseModelName(String cellValue, FullBicycle fullBicycle) {
////        cellValue = cellValue.replace("\r\n", " ").replace("\n", " ").trim();
////        //remove Stels sfrom model name
////
////        cellValue = cellValue.replace("STELS", "").trim();
////        String wheelSize = "0";
////        String modelName = "";
////        Pattern p = Pattern.compile(Constants.PATTERN_STANDART_MODEL_FORMAT);
////        Matcher matchModel = p.matcher(cellValue);
////
////        if (matchModel.find()) {
////           Pattern wheelPattern = Pattern.compile(Constants.PATTERN_WHEEL_SIZE);
////            Matcher wheelMatcher = wheelPattern.matcher(cellValue);
////            if (wheelMatcher.find()) {
////                String extract = wheelMatcher.group().trim();
////                wheelSize = extract.substring(extract.length() - 3, extract.length() - 1);
////            }
////            modelName = cellValue.substring(0, cellValue.length() - 3).trim();
////            modelName = FileReadeHelper.reduceSpaces(modelName);
////            fullBicycle.setModel(modelName);
////
////            fullBicycle.setWheelsSize(wheelSize);
////        } else {
////            LOGGER.error("Non-Standart model string format: " + cellValue);
////            LOGGER.info("Trying alternative way");
////            String model = FileReadeHelper.reduceSpaces(cellValue);
////            model = model.replace(".", "");
////            if (FileReadeHelper.isCrossModel(model)) {
////                FileReadeHelper.processCrossModel(model, fullBicycle);
////            } else if (FileReadeHelper.is275Model(model)) {
////                FileReadeHelper.process175Model(model, fullBicycle);
////            }
////        }
////        fullBicycle.setTrademark(Constants.STELS);
//    }
//
//    public void readFile(String filename) throws PriceReaderException {
//        List<PriceRow> list = new ArrayList<PriceRow>(200);
//        HSSFSheet sheet = getSheet(filename);
//        int numerOfRows = sheet.getPhysicalNumberOfRows();
//
//        for (int i = rowToStart; i < numerOfRows; i++) {
//            PriceRow priceRow = new PriceRow();
//
//            Row row = sheet.getRow(i);
//            if (row == null) {
//                break;
//            }
//            Cell dirtyModelName = row.getCell(modelNameColumnNum);
//            Cell description = row.getCell(descriptionColumnNum);
//            Cell retailPrice = row.getCell(retailPriceColumnNum);
//
//            if (Cell.CELL_TYPE_STRING == dirtyModelName.getCellType()) {
//                String cellValue = dirtyModelName.getStringCellValue();
//                priceRow.setModelName(cellValue);
//            }else {
//                continue;
//            }
//
//            if (Cell.CELL_TYPE_STRING == description.getCellType()) {
//                String cellValue = description.getStringCellValue();
//                priceRow.setDescription(cellValue);
//            }else {
//                continue;
//            }
//
//            if (Cell.CELL_TYPE_NUMERIC == retailPrice.getCellType()) {
//                Double cellValue = retailPrice.getNumericCellValue();
//                priceRow.setRetailPrice(cellValue);
//            }else {
//                continue;
//            }
//
//
//            list.add(priceRow);
//        }
//        processRows(list);
//    }
//
//    private void processRows(List<PriceRow> list){
//
//        SimilarityStrategy strategy = new LevenshteinDistanceStrategy();
//        StringSimilarityService service = new StringSimilarityServiceImpl(strategy);
//
//        for(PriceRow row : list){
//            String dirtyName = row.getModelName();
//            while (dirtyName.contains("  ")){
//                dirtyName = dirtyName.replaceAll("  "," ");
//            }
//            double maxScore = 0.0;
//            String foundModel = "";
//            String toCompare = "";
//            for(String str: knownModelList){
//                 toCompare = ModelComporator.preProcessModel(dirtyName);
//                double score =  service.score(toCompare, str);
//                if(score > maxScore){
//                    maxScore = score;
//                    foundModel = str;
//                }
//            }
//
////            if(maxScore < .9) {
////                System.out.println("to compare: " + toCompare);
//                System.out.println("for [" + dirtyName + "] model is [" + foundModel + "] score is [" + maxScore + "]");
//
////            }
//        }
////        todo:
////        1. Iterate known model and find correct rows
////        2. Construct model object
////        System.out.println(list.size());
//    }
//
//    private HSSFSheet getSheet(String fileName) throws PriceReaderException {
//        HSSFSheet sheet;
//        try {
//            InputStream inputStream = new FileInputStream(fileName);
//            HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
//            sheet = workbook.getSheetAt(0);       // first sheet
//        } catch (FileNotFoundException e) {
//            throw new PriceReaderException("File not found: " + e);
//        } catch (IOException e) {
//            throw new PriceReaderException("Wrong file format: " + e);
//        }
//        return sheet;
//    }
//
//    private List<String> getKnownModelList(){
//        List<String> lines  = null;
//        try{
//            File file = new File("/Users/antonorlov/Documents/StelsParser2014/src/main/resources/model_list_2014.csv");
//            lines = FileUtils.readLines(file, "UTF-8");
//        }catch (IOException ex){
//            ex.printStackTrace();
//        }
//
//        for(int i=0; i< lines.size(); i++){
//            String str = lines.get(i);
//            str =  str.replace(";"," ").trim();
//            lines.set(i,str);
//        }
//        return lines;
//
//    }
//
//
//}
