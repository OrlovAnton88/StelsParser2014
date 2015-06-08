package processor;

import entities.Bicycle;
import entities.FullBicycle;
import excel.PriceReaderException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.CodeGenerator;

import java.util.List;

/**
 * Created by antonorlov on 06/12/14.
 */
public class Processor {

    static final Logger LOGGER = LogManager.getLogger(Processor.class.getName());

    private static Processor instance = null;

    private Processor() {

    }

    public List<FullBicycle> doProcess(List<Bicycle> list) throws PriceReaderException {

        for (Bicycle bicycle : list) {
            String code = CodeGenerator.generateCode(bicycle);
            bicycle.setProdCode(code);
        }
        return null;
    }



    public static Processor getInstance(String priceFileName,
                                            String knownModelsCsvFileName) {
        if (instance == null) {
            instance = new Processor();
        }
        return instance;
    }
}
