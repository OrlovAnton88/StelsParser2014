package ru.antonorlov.csv;


import com.csvreader.CsvWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.antonorlov.entities.Bicycle;
import ru.antonorlov.util.Constants;
import ru.antonorlov.util.PriceReaderException;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Date;

@Component
public class CSVEngine {

    static final Logger LOGGER = LogManager.getLogger(CSVEngine.class);
    @Value("#{T(java.lang.Integer).parseInt('${stels.price.default.sort}')}")
    private int sortValue;

    public void writeFullFile(Collection<Bicycle> bicycles) throws PriceReaderException {

        CsvWriter csvWriter = null;
        try {
            File file = new File(Constants.OUTPUT_FILENAME);
            if (file.exists()) {
                if (!file.delete()) {
                    LOGGER.error("Unable to delete file[" + file.getAbsolutePath() + ']');
                    LOGGER.error("Creating temporary file");
                    file = new File(Constants.RES_FOLDER + "out" + new Date().getTime() + ".csv");
                }
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            csvWriter = new CsvWriter(fileOutputStream, ';', Charset.forName("CP1251"));
            String[] line = CSVEngineHelper.getHeader();
            csvWriter.writeRecord(line);
            for (Bicycle model : bicycles) {
                line = convertModelsIntoArray(model);
                csvWriter.writeRecord(line);
            }
        } catch (FileNotFoundException ex) {
            throw new PriceReaderException("Error writing .csv file. " + ex);

        } catch (IOException ex) {
            throw new PriceReaderException("Error writing .csv file. " + ex);
        } finally

        {
            csvWriter.close();
        }

    }

    public byte[] getFullFile(Collection<Bicycle> bicycles) throws PriceReaderException {
        CsvWriter csvWriter = null;
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            csvWriter = new CsvWriter(outputStream, ';', Charset.forName("CP1251"));
            String[] line = CSVEngineHelper.getHeader();
            csvWriter.writeRecord(line);
            for (Bicycle model : bicycles) {
                line = convertModelsIntoArray(model);
                csvWriter.writeRecord(line);
            }
            csvWriter.flush();
            return outputStream.toByteArray();
        } catch (FileNotFoundException ex) {
            throw new PriceReaderException("Error writing .csv file. " + ex);
        } catch (IOException ex) {
            throw new PriceReaderException("Error writing .csv file. " + ex);
        } finally

        {
            csvWriter.close();
        }


    }


    public void writeCodeAndPriceFile(Collection<Bicycle> bicycles) throws PriceReaderException {

        CsvWriter csvWriter = null;
        try {
            File file = new File(Constants.OUTPUT_FILENAME_CODE_AND_PRICE);
            if (file.exists()) {
                if (!file.delete()) {
                    LOGGER.error("Unable to delete file[" + file.getAbsolutePath() + ']');
                    LOGGER.error("Creating temporary file");
                    file = new File(Constants.RES_FOLDER + "out_code_and_price" + new Date().getTime() + ".csv");
                }
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            csvWriter = new CsvWriter(fileOutputStream, ';', Charset.forName("CP1251"));
            String[] line = {"Код", "Цена"};
            csvWriter.writeRecord(line);
            for (Bicycle bicycle : bicycles) {
                line = new String[]{bicycle.getProductCode(), String.valueOf(bicycle.getPrice())};
                csvWriter.writeRecord(line);

            }
        } catch (FileNotFoundException ex) {
            throw new PriceReaderException("Error writing .csv file. " + ex);

        } catch (IOException ex) {
            throw new PriceReaderException("Error writing .csv file. " + ex);
        } finally

        {
            csvWriter.close();
        }

    }

    private String[] convertModelsIntoArray(Bicycle model) {
        String prodCode = model.getProductCode();
        String[] result = new String[52];
        for (int i = 0; i < 25; i++) {
            result[i] = "";
        }
        result[0] = sortValue+"";
        result[1] = prodCode;
        result[2] = model.getModel();
        result[3] = result[2];
        result[5] = "";
        result[5] = model.getShortDescription();
        result[6] = String.valueOf(model.getPrice());
        result[8] = "-1";
        result[9] = "0";
        result[11] = model.getModel();
        result[12] = "0";
        result[13] = "0";
        result[14] = "1";
        result[16] = "7";
////        result[18] = prodCode + ".jpg," + prodCode + ".jpg," + prodCode + ".jpg";
//        if (model.getImageName() != null) {
            result[18] = model.getImageName() + ',' + model.getImageName() + ',' + model.getImageName();
//        }
        if (model.getImageName2() != null) {
            result[19] = model.getImageName2() + ',' + model.getImageName2() + ',' + model.getImageName2();
        }
        if (model.getImageName3() != null) {
            result[20] = model.getImageName3() + ',' + model.getImageName3() + ',' + model.getImageName3();
        }
        result[25] = "Stels";
        result[26] = model.getWheelsSize();
//        String frameSize = String.valueOf(model.getFrameSize());
//        if (frameSize.equals("0")) {
//            frameSize = "";
//        }
//        result[27] = frameSize;
        result[28] = model.getFrame();
        result[29] = String.valueOf(model.getSpeedsNum());
//        result[30] = model.getColors();
        result[32] = model.getFrontFork();
//        result[34] = model.getSteeringTube();
//        result[35] = model.getBottomBracket();
//        result[36] = model.getCrankset();
//        result[37] = model.getFrontHub();
//        result[38] = model.getRearHub();
//        result[40] = model.getShifters();
//        result[41] = model.getFreeWheel();
        result[42] = model.getFrontDerailleur();
        result[43] = model.getRearDerailleur();
        result[44] = model.getBreaks();
//        result[45] = model.getRims();
//        result[46] = model.getTyres();
        result[47] = model.getFenders();
//        result[48] = model.getPedals();
//        result[49] = model.getSaddle();
//        result[50] = "";
//        result[51] = model.getRack();

        return result;
    }
}
