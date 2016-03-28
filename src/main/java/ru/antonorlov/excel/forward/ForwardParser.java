package ru.antonorlov.excel.forward;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.springframework.stereotype.Service;
import ru.antonorlov.entities.Bicycle;
import ru.antonorlov.entities.PriceRow;
import ru.antonorlov.excel.AbstractParser;
import ru.antonorlov.util.Brand;
import ru.antonorlov.util.PriceReaderException;
import ru.antonorlov.util.Year;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by antonorlov on 26/03/16.
 */
@Service
public class ForwardParser extends AbstractParser {

    @Override
    public List<Bicycle> getBicycles(Year year, String file) throws PriceReaderException {

        List<Bicycle> result = new ArrayList<>();

        HSSFSheet firstSheet = getSheet(file, 0);
        HSSFSheet secondSheet = getSheet(file, 1);

        List<PriceRow> priceRows = getPriceRows(firstSheet, Brand.FORWARD);

        for (PriceRow priceRow : priceRows) {
            Bicycle b = new Bicycle();
            b.setModel(Brand.FORWARD.name() + " " + priceRow.getModelName());
            b.setPrice(getRoundedPrice(priceRow.getRetailPrice()));
            b.setBrand(Brand.FORWARD);
            b.setProductCode(generateProdCode(priceRow.getModelName(), year));

            result.add(b);
        }

        return result;

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

        code.append("_");
        code.append(year.getShortNum());
        return code.toString();
    }

    private void addToCode(final StringBuilder code, String part) {
        part = part.toLowerCase();
        part = part.replace(".", "");
        part = part.replace(",", "");
        part = part.replace("\"", "");
        part = part.replace("\'", "");
        part = part.replace("girl", "g");
        part = part.replace("boy", "b");
        part = part.replace("disc", "d");
        part = part.replace("compact", "com");
        code.append(part);
    }

}
