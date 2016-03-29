package ru.antonorlov.excel.stels;

import com.csvreader.CsvReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.antonorlov.entities.*;
import ru.antonorlov.excel.AbstractParser;
import ru.antonorlov.util.*;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by antonorlov on 11/01/15.
 */
@Component
public class StelsPriceParser extends AbstractParser {

    static final Logger LOGGER = LogManager.getLogger(StelsPriceParser.class.getName());
    List<SimpleBicycle> knownModels;
    List<SimpleBicycle> knownModels2015;
    @Value("${stels.price.file}")
    private String priceFilePath;
    @Value("${stels.known.models}")
    private String knownModelsFilePath;
    @Value("${stels.known.models.2015}")
    private String knownModels2015FilePath;

    @PostConstruct
    public void postConstruct() {
        knownModels = getKnownModelList(knownModelsFilePath);
        knownModels2015 = getKnownModelList(knownModels2015FilePath);
    }

    @Override
    public String getPriceDate(String fileName) {
        try {
            HSSFSheet sheet = getSheet(fileName, 0);

            String marker = "Дата";

            int numOfRows = sheet.getPhysicalNumberOfRows() > 10 ? 10 : sheet.getPhysicalNumberOfRows();

            String dirtyDate = null;

            for (int i = priceConfig.getRowToStart(Brand.STELS); i < numOfRows; i++) {
                Row row = sheet.getRow(i);
                if (row.getPhysicalNumberOfCells() > 1) {
                    Cell cell = row.getCell(0);
                    if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                        String stringCellValue = cell.getStringCellValue();
                        if (stringCellValue.contains(marker)) {
                            dirtyDate = stringCellValue;
                            break;
                        }
                    }
                }
            }
            if (dirtyDate != null) {
                return dirtyDate.trim();
            }

        } catch (Exception ex) {
            LOGGER.error("FAIL to get price date - return today");
            return new Date().toString();
        }
        return new Date().toString();
    }

    public List<DirtyBicycle> getDirtyBicycles(final Year year, final String file) throws PriceReaderException {
        List<DirtyBicycle> toReturn = new ArrayList<DirtyBicycle>();
        HSSFSheet sheet = getSheet(file, 0);
        List<PriceRow> rows = getPriceRows(sheet, Brand.STELS);
        for (PriceRow row : rows) {
            String dirtyName = row.getModelName().trim();
            List<SimpleBicycle> km;
            if (year.equals(Year.YEAR_2016)) {
                km = knownModels;
            } else if (year.equals(Year.YEAR_2015)) {
                km = knownModels2015;
            } else {
                LOGGER.error("YEAR is not defined");
                return Collections.emptyList();
            }
            for (SimpleBicycle simpleBicycle : km) {
                if (simpleBicycle.getDirtyModel().equals(dirtyName.trim())) {
                    DirtyBicycle bicycle = new DirtyBicycle(simpleBicycle);
                    bicycle.setPrice(getRoundedPrice(row.getRetailPrice()));
                    bicycle.setDescription(row.getDescription());
                    bicycle.setProdCode(CodeGenerator.generateCode(bicycle));
                    toReturn.add(bicycle);
                }
            }

        }

        return toReturn;
    }


    public List<Bicycle> getBicycles(final Year year) throws PriceReaderException {
        return getBicycles(year, priceFilePath);
    }

    public List<Bicycle> getBicycles(final Year year, final String filePath) throws PriceReaderException {

        //todo: refactor
        List<DirtyBicycle> list = getDirtyBicycles(year, filePath);

        List<Bicycle> toReturn = new ArrayList<>();
        List<Bicycle> bicycles2016 = transform(list);
        toReturn.addAll(bicycles2016);

        List<DirtyBicycle> dirtyBicycles2015 = getDirtyBicycles(Year.YEAR_2015, filePath);
        List<Bicycle> bicycles2015 = transform(dirtyBicycles2015);
        Util.transformTo2016(bicycles2015);

        toReturn.addAll(bicycles2015);

        return toReturn;
    }

    private List<Bicycle> transform(List<DirtyBicycle> dirtyBicycles) throws PriceReaderException {
        List<Bicycle> toReturn = new ArrayList<>();

        for (DirtyBicycle bicycle : dirtyBicycles) {
            Bicycle b = DescriptionParser.parseDescription(bicycle.getDescription(), bicycle);
            b.setModel("Stels " + b.getModel());
            b.setShortDescription(DescriptionParser.getShortDescription(b));
            b.setBrand(Brand.STELS);
            String imgName = b.getProductCode() + ".jpg";
            b.setImageName(imgName);

            toReturn.add(b);
        }
        return toReturn;
    }


    private List<SimpleBicycle> getKnownModelList(String filename) {
        List<SimpleBicycle> list = new ArrayList<SimpleBicycle>();
        File file = new File(filename);
        try {
            InputStream inputStream = new FileInputStream(file);
//            CsvReader csvReader = new CsvReader(inputStream, Charset.forName("CP1251"));
            CsvReader csvReader = new CsvReader(inputStream, Charset.forName("UTF-8"));
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

    @Override
    protected boolean isRowToBeProcessed(Row row) {
        return true;
    }
}
