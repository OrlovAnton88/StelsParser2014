package ru.antonorlov.excel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.antonorlov.entities.Bicycle;
import ru.antonorlov.entities.PriceRow;
import ru.antonorlov.util.Brand;
import ru.antonorlov.util.PriceConfig;
import ru.antonorlov.util.PriceReaderException;
import ru.antonorlov.util.Year;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by antonorlov on 09/01/16.
 */
@Component
public abstract class AbstractParser {

    public static final String PATTERN_WHEEL_SIZE = "\\d{2}\\\"";
    public static final String PATTERN_MODEL_NUM = "-\\d{3}";
    public static final String PATTERN_MISS_MODEL_NUM = "-\\d{4}";
    public static final String PATTERN_YEAR = "\\.\\d{2}";
    protected static final String PILOT = "Pilot";
    protected static final String NAVIGATOR = "Navigator";
    protected static final String MISS = "Miss";
    static final Logger LOGGER = LogManager.getLogger(AbstractParser.class.getName());
    @Autowired
    protected PriceConfig priceConfig;
    @Value("#{T(java.lang.Integer).parseInt('${stels.price.round.till}')}")
    private int roundTillNum;

    /**
     * Get sheet from file to read
     *
     * @param fileName
     * @return
     * @throws ru.antonorlov.util.PriceReaderException
     */
    protected HSSFSheet getSheet(String fileName, int sheetNum) throws PriceReaderException {
        HSSFSheet sheet;
        try {
            InputStream inputStream = new FileInputStream(fileName);
            HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
            sheet = workbook.getSheetAt(sheetNum);       // first sheet
        } catch (FileNotFoundException e) {
            throw new PriceReaderException("File not found: " + e);
        } catch (IOException e) {
            throw new PriceReaderException("Wrong file format: " + e);
        }
        return sheet;
    }

    protected List<PriceRow> getPriceRows(final HSSFSheet sheet,
                                          final Brand brand) {

        List<PriceRow> list = new ArrayList<PriceRow>(200);

        FormulaEvaluator evaluator = sheet.getWorkbook().getCreationHelper().createFormulaEvaluator();

        int numOfRows = sheet.getPhysicalNumberOfRows();

        for (int i = priceConfig.getRowToStart(brand); i < numOfRows; i++) {
            PriceRow priceRow = new PriceRow();

            Row row = sheet.getRow(i);
            if (row == null) {
                break;
            }
            Cell dirtyModelName = row.getCell(priceConfig.getModelNameColumn(brand));
            int descriptionColumnNum = priceConfig.getDescriptionColumnNum(brand);
            if (descriptionColumnNum != -1) {
                Cell description = row.getCell(descriptionColumnNum);
                if (Cell.CELL_TYPE_STRING == description.getCellType()) {
                    String cellValue = description.getStringCellValue();
                    priceRow.setDescription(cellValue);
                } else {
                    //do nothing - optional field
                }
            }


            Cell price = row.getCell(priceConfig.getPriceColumn(brand));

            if (Cell.CELL_TYPE_STRING == dirtyModelName.getCellType()) {
                String cellValue = dirtyModelName.getStringCellValue();
                priceRow.setModelName(cellValue);
            } else {
                continue;
            }


            if (Cell.CELL_TYPE_NUMERIC == price.getCellType()) {
                Double cellValue = price.getNumericCellValue();
                priceRow.setRetailPrice(cellValue);
            } else {
                if (Cell.CELL_TYPE_STRING == price.getCellType()) {
                    String s = price.getStringCellValue();
                    double p = 0.0;
                    if (s != null) {
                        s = s.replaceAll("\u00A0", "").replaceAll(",", ".");
                        try {
                            p = Double.valueOf(s);
                        } catch (NumberFormatException ex) {
                            LOGGER.error("Error parsing price[" + s + "]", ex);
                        }
                    }
                    priceRow.setRetailPrice(p);

                } else {
                    int cellType = evaluator.evaluateFormulaCell(price);
                    if (Cell.CELL_TYPE_NUMERIC == cellType) {
                        Double cellValue = price.getNumericCellValue();
                        priceRow.setRetailPrice(cellValue);
                    } else {
                        priceRow.setRetailPrice(0.0);
                    }
                }
            }

            list.add(priceRow);
        }
        return list;

    }

    protected int getRoundedPrice(final Double originalPrice) {
        int roundedPrice = 0;
        if (originalPrice != null && originalPrice != 0.0) {
            roundedPrice = (originalPrice.intValue() / roundTillNum) * 10;
        }
        return roundedPrice;
    }

    public abstract String getPriceDate(String fileName);

    public abstract List<Bicycle> getBicycles(final Year year, final String file) throws PriceReaderException;
}
