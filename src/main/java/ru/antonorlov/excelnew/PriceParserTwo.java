package ru.antonorlov.excelnew;

import com.csvreader.CsvReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.antonorlov.entities.*;
import ru.antonorlov.excel.PriceReaderException;
import ru.antonorlov.util.CodeGenerator;
import ru.antonorlov.util.DescriptionParser;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by antonorlov on 11/01/15.
 */
@Component
public class PriceParserTwo extends AbstractParser {

    static final Logger LOGGER = LogManager.getLogger(PriceParserTwo.class.getName());
    List<SimpleBicycle> knownModels;
    @Value("${stels.price.file}")
    private String priceFilePath;
    @Value("${stels.known.models}")
    private String knownModelsFilePath;
    @Value("#{T(java.lang.Integer).parseInt('${stels.price.round.till}')}")
//    @Value("stels.row_to_start")
    private int roundTillNum;
    @Value("#{T(java.lang.Integer).parseInt('${stels.row_to_start}')}")
//    @Value("stels.row_to_start")
    private int rowToStart;
    @Value("#{T(java.lang.Integer).parseInt('${stels.model_name_column_number}')}")
    private int modelNameColumnNum;
    @Value("#{T(java.lang.Integer).parseInt('${stels.description_column_number}')}")
//    @Value("stels.description_column_number")
    private int descriptionColumnNum;
    @Value("#{T(java.lang.Integer).parseInt('${stels.price_column_number}')}")
//    @Value("stels.price_column_number")
    private int priceColumnNum;
    private int price400ColumnNum = 0;

    @PostConstruct
    public void postConstruct() {
        knownModels = getKnownModelList(knownModelsFilePath);
    }

    public List<PriceRow> getPriceRows() throws Exception {
        HSSFSheet sheet = getSheet(priceFilePath);
        List<PriceRow> rows = getDirtyModels(sheet);
        return rows;
    }

    public List<Bicycle> getBicycles() throws PriceReaderException {
        List<Bicycle> toReturn = new ArrayList<Bicycle>();
        HSSFSheet sheet = getSheet(priceFilePath);
        List<PriceRow> rows = getDirtyModels(sheet);
        for (PriceRow row : rows) {
            String dirtyName = row.getModelName().trim();
            for (SimpleBicycle simpleBicycle : knownModels) {
                if (simpleBicycle.getDirtyModel().equals(dirtyName.trim())) {
                    Bicycle bicycle = new Bicycle(simpleBicycle);
                    int price =  0 ;
                    Double retailPrice = row.getRetailPrice();
                    if( retailPrice != null && retailPrice != 0.0){
                        price = ( retailPrice.intValue() / roundTillNum) * 10;
                    }
                    bicycle.setPrice(price);
                    bicycle.setDescription(row.getDescription());
                    bicycle.setProdCode(CodeGenerator.generateCode(bicycle));
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
            b.setModel("Stels " + b.getModel());
            b.setShortDescription(DescriptionParser.getShortDescription(b));

            String imgName = b.getProductCode() + ".jpg";
            b.setImageName(imgName);

            toReturn.add(b);
        }
        return toReturn;
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
//            Cell price400 = row.getCell(price400ColumnNum);

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
                if (Cell.CELL_TYPE_STRING == price.getCellType()) {
                    String s = price.getStringCellValue();
                    double p = 0.0;
                    if (s != null) {
                        s = s.replaceAll("\u00A0", "").replaceAll(",",".");
                        try {
                            p = Double.valueOf(s);
                        } catch (NumberFormatException ex) {
                            LOGGER.error("Error parsing price[" + s + "]", ex);
                        }
                    }
                        priceRow.setRetailPrice(p);

                } else {
                    priceRow.setRetailPrice(0.0);
                }
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


}
