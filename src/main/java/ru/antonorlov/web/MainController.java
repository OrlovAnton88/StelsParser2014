package ru.antonorlov.web;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import ru.antonorlov.csv.CSVEngine;
import ru.antonorlov.entities.Bicycle;
import ru.antonorlov.excel.stels.StelsPriceParser;
import ru.antonorlov.util.Util;
import ru.antonorlov.util.Year;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by antonorlov on 09/01/16.
 */
@Controller
public class MainController {

    private static final Logger LOGGER = LogManager.getLogger(MainController.class.getName());
    @Autowired
    private CSVEngine csvEngine;
    @Value("${stels.price.file}")
    private String priceFilePath;

    @Value("${upload.directory}")
    private String uploadDir;

    @Autowired
    private StelsPriceParser parser;

    @RequestMapping("/")
    public ModelAndView index() {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("name", "testName");
        File f = new File(priceFilePath);
        mv.addObject("priceFileName", priceFilePath);

        mv.addObject("files", getUploadedFiles());
        mv.addObject("priceFileExists", f.exists());

        return mv;
    }

    /**
     * Предпросмотр прайса
     * @return
     */
    @RequestMapping("/preview")
    public ModelAndView preview() {
        ModelAndView mv = new ModelAndView("preview");
        try {
            List<Bicycle> fullBicycles = parser.getBicycles(Year.YEAR_2016);
            Set<Bicycle> result = addTransformed2015Model(fullBicycles);

            mv.addObject("list", result);
        } catch (Exception ex) {
            mv.addObject("errorMsg", ex.getMessage());
            LOGGER.error("Fail to preview", ex);
        }

        return mv;
    }

    @RequestMapping("/parse")
    public ModelAndView parse() {
        return new ModelAndView("index");
    }

    @RequestMapping("/fullCsv")
    public HttpEntity<byte[]> fullCsv() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd_MM_yyyy");

        String csvFileName = "full_stels_" + sdf.format(new Date()) + ".csv";

        HttpHeaders header = new HttpHeaders();

        // creates mock data
        header.setContentType(new MediaType("application", "csv"));

        header.set("Content-Disposition",
                "attachment; filename=" + csvFileName);


        List<Bicycle> fullBicycles = null;

        try {
            fullBicycles = parser.getBicycles(Year.YEAR_2016);
        } catch (Exception ex) {
            LOGGER.error("Fail to get full bicycles", ex);
        }

        Set<Bicycle> result = addTransformed2015Model(fullBicycles);

        if (fullBicycles != null) {
            try {
                byte[] bytes = csvEngine.getFullFile(result);

                return new HttpEntity<byte[]>(bytes, header);

            } catch (Exception ex) {
                LOGGER.error("Fail to get CSV file", ex);
            }

        }
        return new ResponseEntity<byte[]>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private List<File> getUploadedFiles(){
        File dir = new File(uploadDir);
        List<File> files = new ArrayList<>();
        if(dir != null && dir.listFiles()!= null) {
            for (File file : dir.listFiles()) {
                if (file.isFile()) {
                    files.add(file);
                }
            }
        }
        return files;
    }

    private Set<Bicycle> addTransformed2015Model(List<Bicycle> models2016){

        try {
            List<Bicycle> fullBicycles2015 = parser.getBicycles(Year.YEAR_2015);

            Util.transformTo2016(fullBicycles2015);

            Set<Bicycle> result = new HashSet<>(models2016);
            for (Bicycle b : fullBicycles2015) {
                if (b.getProductCode().equals("sarrow1616")) {
                    System.out.println("sarrow1616");
                }
                if (!result.contains(b)) {
                    result.add(b);
                }
            }
            return result;
        }catch (Exception ex){
            LOGGER.error("Fail to add transformed 2015 models ");
        }
        return Collections.emptySet();
    }
}
