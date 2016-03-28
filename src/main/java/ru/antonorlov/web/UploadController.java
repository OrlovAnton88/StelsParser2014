package ru.antonorlov.web;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import ru.antonorlov.entities.Bicycle;
import ru.antonorlov.entities.PriceList;
import ru.antonorlov.excel.forward.ForwardParser;
import ru.antonorlov.excel.stels.StelsPriceParser;
import ru.antonorlov.service.PriceListService;
import ru.antonorlov.util.Brand;
import ru.antonorlov.util.Year;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * Created by antonorlov on 10/01/16.
 */

@Controller
public class UploadController {

    private static final Logger LOGGER = LogManager.getLogger(UploadController.class.getName());
    @Value("${upload.directory}")
    private String uploadDir;
    @Autowired
    private StelsPriceParser stelsPriceParser;
    @Autowired
    private ForwardParser forwardParser;
    @Autowired
    private PriceListService priceListService;

    @RequestMapping(value = "/uploadPrice", method = RequestMethod.POST)
    public
    @ResponseBody
    String handleFileUpload(@RequestParam("file") MultipartFile file,
                            @RequestParam("brand") String brandStr) {
        String name = file.getOriginalFilename();
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();

                final String path = uploadDir + "/" + name;
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(new File(path)));
                stream.write(bytes);
                stream.close();

//                Thread thread = new Thread() {
//                    public void run() {
//                        try {
                final List<Bicycle> bicycles;

                if (brandStr.equals(Brand.STELS.name())) {
                    bicycles = stelsPriceParser.getBicycles(Year.YEAR_2016, path);
                } else if (brandStr.equals(Brand.FORWARD.name())) {
                    bicycles = forwardParser.getBicycles(Year.YEAR_2016, path);
                } else {
                    bicycles = null;
                }

                if (bicycles != null) {
                    String priceDate = stelsPriceParser.getPriceDate(path);
                    PriceList pl = new PriceList();
                    pl.setName(name);
                    pl.setDate(priceDate);
                    pl.setBicycles(bicycles);
                    priceListService.savePriceList(pl);
                }
//                        } catch (Exception ex) {
//                            LOGGER.error(ex);
//                        }
//                    }
//                };
//                thread.start();
                return "You successfully uploaded " + name + "!";
            } catch (Exception e) {
                return "You failed to upload " + name + " => " + e.getMessage();
            }
        } else {
            return "You failed to upload " + name + " because the file was empty.";
        }
    }

}
