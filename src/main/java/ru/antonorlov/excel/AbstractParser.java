package ru.antonorlov.excel;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import ru.antonorlov.util.PriceReaderException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by antonorlov on 09/01/16.
 */
public abstract class AbstractParser {

    public static final String PATTERN_WHEEL_SIZE = "\\d{2}\\\"";
    public static final String PATTERN_MODEL_NUM = "-\\d{3}";
    public static final String PATTERN_MISS_MODEL_NUM = "-\\d{4}";
    public static final String PATTERN_YEAR = "\\.\\d{2}";
    protected static final String PILOT = "Pilot";
    protected static final String NAVIGATOR = "Navigator";
    protected static final String MISS = "Miss";

    /**
     * Get sheet from file to read
     *
     * @param fileName
     * @return
     * @throws ru.antonorlov.util.PriceReaderException
     */
    protected HSSFSheet getSheet(String fileName) throws PriceReaderException {
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
}
