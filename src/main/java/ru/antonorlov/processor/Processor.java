package ru.antonorlov.processor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.antonorlov.entities.Bicycle;
import ru.antonorlov.entities.FullBicycle;
import ru.antonorlov.excel.PriceReaderException;
import ru.antonorlov.util.CodeGenerator;

import java.util.List;

/**
 * Created by antonorlov on 06/12/14.
 */
public class Processor {

    static final Logger LOGGER = LogManager.getLogger(Processor.class.getName());

    private static Processor instance = null;

    private Processor() {

    }

    public static Processor getInstance(String priceFileName,
                                            String knownModelsCsvFileName) {
        if (instance == null) {
            instance = new Processor();
        }
        return instance;
    }

    public List<FullBicycle> doProcess(List<Bicycle> list) throws PriceReaderException {

        for (Bicycle bicycle : list) {
            String code = CodeGenerator.generateCode(bicycle);
            bicycle.setProdCode(code);
        }
        return null;
    }
}
