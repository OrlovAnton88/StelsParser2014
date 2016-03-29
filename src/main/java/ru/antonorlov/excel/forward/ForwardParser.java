package ru.antonorlov.excel.forward;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;
import ru.antonorlov.entities.Bicycle;
import ru.antonorlov.entities.PriceRow;
import ru.antonorlov.excel.AbstractParser;
import ru.antonorlov.util.Brand;
import ru.antonorlov.util.DescriptionParser;
import ru.antonorlov.util.PriceReaderException;
import ru.antonorlov.util.Year;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by antonorlov on 26/03/16.
 */
@Service
public class ForwardParser extends AbstractParser {

    static final org.apache.logging.log4j.Logger LOGGER = org.apache.logging.log4j.LogManager.getLogger(ForwardParser.class.getName());


    @Override
    public List<Bicycle> getBicycles(Year year, String file) throws PriceReaderException {

        List<Bicycle> result = new ArrayList<>();

        HSSFSheet firstSheet = getSheet(file, 0);
        HSSFSheet secondSheet = getSheet(file, 1);

        List<PriceRow> priceRows = getPriceRows(firstSheet, Brand.FORWARD);

        for (PriceRow priceRow : priceRows) {
            Bicycle b = new Bicycle();
            Brand brand = priceRow.getBrand() == null ? Brand.FORWARD : priceRow.getBrand();
            if (priceRow.getModelName().contains(brand.name())) {
                //иногда в названии модели уже указан бренд
                b.setModel(priceRow.getModelName() + year.getFullNum());
            } else {
                b.setModel(brand + " " + priceRow.getModelName() +  year.getFullNum());
            }
            if (priceRow.getRetailPrice() == 0) {
                LOGGER.warn("Price for [" + priceRow.getModelName() + "] is 0. Skipping...");
                continue;
            }
            b.setPrice(getRoundedPrice(priceRow.getRetailPrice()));
            b.setBrand(brand);
            b.setProductCode(generateProdCode(priceRow.getModelName(), year));
            String imgName = b.getProductCode() + ".jpg";
            b.setImageName(imgName);
            result.add(b);
        }

        //обавляем спецификацию - рама, колеса и тп
        fillSpecification(secondSheet, result);

        result.stream().forEach(b -> b.setShortDescription(DescriptionParser.getShortDescription(b)));
        return result;

    }

    @Override
    protected boolean isRowToBeProcessed(Row row) {
        //отфильтруем все что не 2016
        Cell cell = row.getCell(1);
        if(cell != null){
            if(Cell.CELL_TYPE_NUMERIC == cell.getCellType()){
                int i = ((Double) cell.getNumericCellValue()).intValue();
                if(i != 2016){
                    if(row.getCell(2).getStringCellValue().contains("compact")){
                        return true;
                    }
                    return false;
                }
            }else if(Cell.CELL_TYPE_STRING == cell.getCellType()){
                if(!cell.getStringCellValue().equals("2016")){
                    //компакты оставляем
                    if(row.getCell(2).getStringCellValue().contains("compact")){
                        return true;
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private void fillSpecification(final HSSFSheet sheet, final List<Bicycle> bicycles) {
        int rowToStart = 5;
        int modelCol = 2;
        int frameSizeCol = 3;
        int set1 = 4;
        int set2 = 5;
        int wheelsCol = 6;
        int frameCol = 7;
        int numOfSpeedCol = 8;

        Map<String, Bicycle> map = bicycles.stream().collect(Collectors.toMap(b -> b.getModel(), b -> b));

        int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();
        for (int i = rowToStart; i < physicalNumberOfRows; i++) {
            Bicycle bicycle = null;
            HSSFRow row = sheet.getRow(i);
            Cell modelCell = row.getCell(modelCol);
            if (modelCell == null) {
                continue;
            }
            if (Cell.CELL_TYPE_STRING == modelCell.getCellType()) {
                String value = modelCell.getStringCellValue();
                bicycle = map.get(value != null ? Brand.FORWARD.name() + " " + value.trim() : null);
                if (bicycle == null) {
                    bicycle = map.get(value != null ? value.trim() : null);

                }
            }
            if (bicycle == null) {
                LOGGER.error("Model [" + modelCell.toString() + "] not found in map");
                continue;
            }

            Cell frameSizeCell = row.getCell(frameSizeCol);
            if (Cell.CELL_TYPE_STRING == frameSizeCell.getCellType()) {
                String value = frameSizeCell.getStringCellValue();
                if (value != null) {
                    bicycle.setFrameSize(value);
                }
            } else if (Cell.CELL_TYPE_NUMERIC == frameSizeCell.getCellType()) {
                //sometimes it's number
                Double val = frameSizeCell.getNumericCellValue();
                bicycle.setFrame(val.intValue() + "");


            }

            HSSFCell wheelCell = row.getCell(wheelsCol);
            if (Cell.CELL_TYPE_STRING == wheelCell.getCellType()) {
                String value = wheelCell.getStringCellValue();
                if (value.charAt(value.length() - 1) == '"') {
                    value = value.substring(0, value.length() - 1);
                }
                bicycle.setWheelsSize(value);
            }
            //todo: 27,5 agris  - number

            HSSFCell frameCell = row.getCell(frameCol);
            if (frameCell != null && Cell.CELL_TYPE_STRING == frameCell.getCellType()) {
                try {
                    bicycle.setFrame(frameCell.getStringCellValue());
                } catch (NullPointerException ex) {
                    System.out.println(ex);
                }
            }

            HSSFCell numOfSpeedsCell = row.getCell(numOfSpeedCol);
            if (Cell.CELL_TYPE_STRING == numOfSpeedsCell.getCellType()) {
                try {
                    bicycle.setSpeedsNum(Integer.valueOf(numOfSpeedsCell.getStringCellValue()));
                } catch (NumberFormatException ex) {
                    //shit happens
                }

            } else if (Cell.CELL_TYPE_NUMERIC == numOfSpeedsCell.getCellType()) {
                Double val = numOfSpeedsCell.getNumericCellValue();
                bicycle.setSpeedsNum(val.intValue());
            }

        }

    }

    @Override
    public String getPriceDate(String fileName) {
        return null;
    }


    private String generateProdCode(final String modelName, final Year year) {
        final StringBuilder code = new StringBuilder("");

        code.append("f_");

        List<String> strings = Arrays.asList(modelName.split(" "));

        strings.stream().forEach(part -> addToCode(code, part)
        );

        code.append(year.getShortNum());
        return code.toString();
    }

    private void addToCode(final StringBuilder code, String part) {
        part = part.toLowerCase();
        part = part.replace(".", "");
        part = part.replace(",", "");
        part = part.replace(")", "");
        part = part.replace("(", "");
        part = part.replace("\"", "");
        part = part.replace("\'", "");
        part = part.replace("girl", "g");
        part = part.replace("boy", "b");
        part = part.replace("disc", "d");
        part = part.replace("compact", "com");
        code.append(part);
    }

}
