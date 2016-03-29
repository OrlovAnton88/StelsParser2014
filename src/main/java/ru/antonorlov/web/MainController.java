package ru.antonorlov.web;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import ru.antonorlov.csv.CSVEngine;
import ru.antonorlov.entities.PriceList;
import ru.antonorlov.excel.stels.StelsPriceParser;
import ru.antonorlov.service.PriceListService;
import ru.antonorlov.util.Brand;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by antonorlov on 09/01/16.
 */
@Controller
public class MainController {

    private static final Logger LOGGER = LogManager.getLogger(MainController.class.getName());
    @Autowired
    private CSVEngine csvEngine;

    @Autowired
    private PriceListService priceListService;

    @Value("${stels.price.file}")
    private String priceFilePath;

    @Value("${upload.directory}")
    private String uploadDir;

    @Autowired
    private StelsPriceParser parser;

    @RequestMapping("/")
    public ModelAndView index() {
        ModelAndView mv = new ModelAndView("index");
        List<PriceList> priceLists = priceListService.gerAllPriceLists();
        mv.addObject("priceLists", priceLists);
        return mv;
    }

    /**
     * Предпросмотр прайса
     *
     * @return
     */
    @RequestMapping("/preview/{priceId}")
    public ModelAndView preview(@PathVariable Integer priceId) {
        ModelAndView mv = new ModelAndView("preview");

        Optional<PriceList> priceList = priceListService.getPriceList(priceId);
        if (priceList.isPresent()) {
            mv.addObject("priceList", priceList.get());
        }

        return mv;
    }

    @RequestMapping("/parse")
    public ModelAndView parse() {
        return new ModelAndView("index");
    }

    @RequestMapping("/fullCsv/{priceId}")
    public HttpEntity<byte[]> fullCsv(@PathVariable Integer priceId) throws Exception {

        final String csvFileName;
        SimpleDateFormat sdf = new SimpleDateFormat("dd_MM_yyyy");
        Optional<PriceList> priceList = priceListService.getPriceList(priceId);
        if (priceList.isPresent()) {
            Brand brand = priceList.get().getBicycles().get(0).getBrand();
            //todo: date
            csvFileName = brand.name().toLowerCase() + sdf.format(new Date()) + ".csv";

            HttpHeaders header = new HttpHeaders();

            // creates mock data
            header.setContentType(new MediaType("application", "csv"));

            header.set("Content-Disposition",
                    "attachment; filename=" + csvFileName);

            try {
                byte[] bytes = csvEngine.getFullFile(priceList.get().getBicycles());
                return new HttpEntity<byte[]>(bytes, header);
            } catch (Exception ex) {
                LOGGER.error("Fail to get CSV file", ex);
                return new ResponseEntity<byte[]>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } else {
            return new ResponseEntity<byte[]>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    private List<File> getUploadedFiles() {
        File dir = new File(uploadDir);
        List<File> files = new ArrayList<>();
        if (dir != null && dir.listFiles() != null) {
            for (File file : dir.listFiles()) {
                if (file.isFile()) {
                    files.add(file);
                }
            }
        }
        return files;
    }

}
