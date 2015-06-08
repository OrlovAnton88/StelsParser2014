package excelnew;

import com.csvreader.CsvReader;
import entities.*;
import excel.PriceReaderException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import util.CodeGenerator;
import util.DescriptionParser;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by antonorlov on 11/01/15.
 */
public class PriceParserTwo {


    static final Logger LOGGER = LogManager.getLogger(PriceParser.class.getName());
    private static PriceParserTwo instance = null;

    private String priceFileName;
    private String knownModelsCsvFileName;
    List<SimpleBicycle> knownModels;

    private boolean init;

    private int rowToStart = 4;

    private int modelNameColumnNum = 1;
    private int descriptionColumnNum = 2;
    private int priceColumnNum = 6;
    private int price400ColumnNum = 0;
//    private static final int price1blnColumnNum = 4;
//    private static final int price1_5blnColumnNum = 3;


    private static final String PILOT = "Pilot";
    private static final String NAVIGATOR = "Navigator";
    private static final String MISS = "Miss";
    public static final String PATTERN_WHEEL_SIZE = "\\d{2}\\\"";
    public static final String PATTERN_MODEL_NUM = "-\\d{3}";
    public static final String PATTERN_MISS_MODEL_NUM = "-\\d{4}";
    public static final String PATTERN_YEAR = "\\.\\d{2}";

    private PriceParserTwo(String priceFileName,
                           String knownModelsCsvFileName) {
        this.priceFileName = priceFileName;
        this.knownModelsCsvFileName = knownModelsCsvFileName;


        knownModels = getKnownModelList(knownModelsCsvFileName);

    }

    public List<PriceRow> getPriceRows() throws Exception{
        HSSFSheet sheet = getSheet(priceFileName);
        List<PriceRow> rows = getDirtyModels(sheet);
        return rows;
    }

    public List<Bicycle> getBicycles() throws PriceReaderException {
        if (!init) {
            throw new IllegalStateException("No sheet params specified!");
        }


        List<Bicycle> toReturn = new ArrayList<Bicycle>();

        HSSFSheet sheet = getSheet(priceFileName);
        List<PriceRow> rows = getDirtyModels(sheet);
        for (PriceRow row : rows) {
            String dirtyName = row.getModelName().trim();
            for (SimpleBicycle simpleBicycle : knownModels) {
                if (simpleBicycle.getDirtyModel().equals(dirtyName.trim())) {
                    Bicycle bicycle = new Bicycle(simpleBicycle);
                    bicycle.setPrice(row.getRetailPrice().intValue());
                    bicycle.setDescription(row.getDescription());
                    bicycle.setProdCode(CodeGenerator.generateCode(bicycle));
                    ;
                    toReturn.add(bicycle);
                }
            }

        }

        return toReturn;
    }


    public List<FullBicycle> getFullBicycles() throws Exception {
        List<Bicycle> list = getBicycles();
        List<FullBicycle> toReturn = new ArrayList<FullBicycle>();

        for (Bicycle bicycle : list) {
            FullBicycle b = DescriptionParser.parseDescription(bicycle.getDescription(), bicycle);
            b.setModel("Stels " +b.getModel());
            b.setShortDescription(DescriptionParser.getShortDescription(b));

            toReturn.add(b);
        }
        return toReturn;
    }

    public PriceParserTwo setSheetParams(int rowToStart,
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


    public PriceParserTwo setSheetParams(int rowToStart,
                                         int modelNameColumnNum,
                                         int descriptionColumnNum,
                                         int priceColumnNum,
                                         int price400) {

        this.rowToStart = rowToStart;
        this.modelNameColumnNum = modelNameColumnNum;
        this.descriptionColumnNum = descriptionColumnNum;
        this.priceColumnNum = priceColumnNum;
        this.price400ColumnNum = price400;
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
            Cell price400 = row.getCell(price400ColumnNum);

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

            if (Cell.CELL_TYPE_NUMERIC == price400.getCellType()) {
                Double cellValue = price400.getNumericCellValue();
                priceRow.setPrice400(cellValue);
            } else {
                continue;
            }


            list.add(priceRow);
        }
        return list;

    }

    private List<SimpleBicycle> getKnownModelList(String filename) {
        List<SimpleBicycle> list = new ArrayList<SimpleBicycle>();
        File file = new File(filename);
        try {
            InputStream inputStream = new FileInputStream(file);
            CsvReader csvReader = new CsvReader(inputStream, Charset.forName("CP1251"));
            csvReader.setDelimiter(';');
            while (csvReader.readRecord()) {
                String[] values = csvReader.getValues();
                SimpleBicycle simpleBicycle = new SimpleBicycle();
                simpleBicycle.setDirtyModel(values[0].trim());
                simpleBicycle.setModel(values[1].trim());
                simpleBicycle.setWheelSize(WheelSize.getSizeByValue(values[2].trim()));
                list.add(simpleBicycle);

            }

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return list;

    }


    public static PriceParserTwo getInstance(String priceFileName,
                                             String knownModelsCsvFileName) {
        if (instance == null) {
            instance = new PriceParserTwo(priceFileName, knownModelsCsvFileName);
        }
        return instance;
    }


}
