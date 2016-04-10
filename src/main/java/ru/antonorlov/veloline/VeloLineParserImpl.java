package ru.antonorlov.veloline;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.antonorlov.entities.Bicycle;
import ru.antonorlov.service.BicycleService;

import java.io.IOException;
import java.util.List;

/**
 * Created by antonorlov on 08/04/16.
 */
@Component
public class VeloLineParserImpl implements VeloLineParser {

    public static final String SEARCH_URL = "http://www.velo-line.ru/index.php?searchstring=";
    static final Logger LOGGER = LogManager.getLogger(VeloLineParserImpl.class.getName());
    @Autowired
    private BicycleService bicycleService;

    @Override
    public void fillWithPublishInfo(List<Bicycle> list) {
        list.stream().forEach(b -> addBicycleUrl(b));
    }


    private void addBicycleUrl(final Bicycle bicycle) {

        //wait to decrease load on web-site

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            LOGGER.error("Fail to wait", e);
        }

        final String model = bicycle.getModel();

        final String searchParam = model.replaceAll("\\s", "+");

//        Stels+Navigator+500+2016

        Document doc;
        try {
            final String url = SEARCH_URL + searchParam;
            doc = Jsoup.connect(url).get();

            isOneProductFound(doc, searchParam);
            Elements select = doc.select(".boxleft a");

            if (select.size() != 1) {
                if (select.isEmpty()) {
                    LOGGER.error("No link found on the page[" + url + "]");
                } else {
                    LOGGER.error("More than one link found on the page[" + url + "]");

                }
            }
            Element link = select.get(0);
            if (model.equals(link.text())) {
                String href = link.attr("href");
                if (href == null || href.isEmpty()) {
                    LOGGER.error("No href attr found");
                }

                bicycle.setUrl(href);
                bicycleService.updateBycycle(bicycle);
            } else {
                LOGGER.error("Model" + model + " is not equal to link text[" + link.text() + "]");
            }


        } catch (IOException ex) {
            LOGGER.error("ex");
        }


    }

    private boolean isOneProductFound(final Document doc,
                                      final String url) {
        String text = doc.select(".cattop b").text();

        try {
            Integer integer = Integer.valueOf(text);
            if (integer.intValue() != 1) {
                LOGGER.error("More than one product found - skipping. URL[" + SEARCH_URL + url + "]");
                return false;
            }
        } catch (NumberFormatException ex) {
            //product not found
            LOGGER.error("No product found - skipping URL[" + SEARCH_URL + url + "]");
            return false;
        }
        return false;
    }


}
