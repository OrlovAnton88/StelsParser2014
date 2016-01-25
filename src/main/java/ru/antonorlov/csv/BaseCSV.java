package ru.antonorlov.csv;

import com.csvreader.CsvWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.antonorlov.entities.TmpBike;
import ru.antonorlov.entities.WheelSize;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Created by antonorlov on 12/01/15.
 */
public class BaseCSV {

    static final Logger LOGGER = LogManager.getLogger(BaseCSV.class.getName());

    public void write(List<TmpBike> list) {
        CsvWriter csvWriter = null;

        File file = new File("base.csv");
        if (file.exists()) {
            if (!file.delete()) {
                LOGGER.error("Unable to delete file[" + file.getAbsolutePath() + ']');
                LOGGER.error("Creating temporary file");
                return;
            }
        }

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            csvWriter = new CsvWriter(fileOutputStream, ';', Charset.forName("CP1251"));

            for (TmpBike tmpBike : list) {
                String[] line = convertIntoArray(tmpBike);
                csvWriter.writeRecord(line);
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            csvWriter.close();
        }

    }

    private String[] convertIntoArray(TmpBike tmpBike) {
        String[] array = new String[3];

        if (tmpBike == null) {
            return array;
        }

        if (tmpBike.getDirtyModel() != null) {
            array[0] = tmpBike.getDirtyModel();
        } else {
            array[0] = "N/A";
        }
        if (tmpBike.getModelNum() != null) {
            String modelName = "";
            String type = tmpBike.getType();
            if (type == null) {
                type = "";
            } else {
                type = type + " ";
            }

            String wheelSize = "";

            String comment = tmpBike.getComment();
            if (comment == null) {
                comment = "";
            }
            if (tmpBike.getWheelSize().equals(WheelSize.TWELWE) ||
                    tmpBike.getWheelSize().equals(WheelSize.FOURTEEN) ||
                    tmpBike.getWheelSize().equals(WheelSize.SIXTEEN) ||
                    tmpBike.getWheelSize().equals(WheelSize.EIGHTTEEN)) {
                wheelSize = tmpBike.getWheelSize().getSize() + "\"" + " ";
            }

            modelName = type + tmpBike.getModelNum() + " " + wheelSize + tmpBike.getYear() + " " + comment;
            array[1] = modelName.trim();
        } else {
            array[1] = "N/A";
        }

        if (tmpBike.getWheelSize() != null) {
            array[2] = tmpBike.getWheelSize().getSize();
        } else {
            array[2] = "";
        }

        return array;
    }
}
