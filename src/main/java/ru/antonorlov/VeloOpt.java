package ru.antonorlov;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.antonorlov.entities.PriceRow;

import java.util.List;

/**
 * Created by antonorlov on 01/02/15.
 */
public class VeloOpt {

    private static final String PRICE_FILE = "/Users/antonorlov/Documents/StelsParser2014/src/main/resources/price_13_01.xls";
    private static final String KNOWN_MODELS_FILE = "/Users/antonorlov/Documents/StelsParser2014/src/main/resources/base.csv";

    private static final String PAGE = "<!DOCTYPE html>\n" +
            "<html>\n" +
            "<head lang=\"en\">\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <title>Велосипеды Stels оптом в СПб | Прайс</title>\n" +
            "    <meta name=\"description\" content=\"Велосипеды Stels оптом в СПб\">\n" +
            "    <meta name=\"keywords\" content=\"Велосипеды Стелс оптом, велосипеды Stels, Stels Naviator,Stels Pilot\">\n" +
            "    <link href=\"css/bootstrap.min.css\" rel=\"stylesheet\">\n" +
            "    <link href=\"css/style.css\" rel=\"stylesheet\">\n" +
            "</head>\n" +
            "<body>\n" +
            "<div class=\"container-fluid\">\n" +
            "    <div class=\"page-header\">\n" +
            "        <h1>Велосипеды Stels оптом\n" +
            "            <small>в Санкт-Петербурге и Лен. области</small>\n" +
            "        </h1>\n" +
            "    </div>\n" +
            "    <ul class=\"nav nav-tabs\">\n" +
            "        <li role=\"presentation\"><a href=\"index.html\">Главная</a></li>\n" +
            "        <li role=\"presentation\" class=\"active\"><a href=\"price.html\">Прайс</a></li>\n" +
            "    <li role=\"presentation\"><a href=\"http://www.velo-line.ru/\" target=\"_blank\">Велосипеды Stels в розницу</a></li>" +
            "    </ul>\n" +
            "\n" +
            "    <div class=\"row bordered\">\n" +
            "        <div class=\"col-md-12\" id=\"price\">\n" +
            "\n" +
            "        </div>\n" +
            "    </div>\n" +
            "</div>\n" +
            "</body>\n" +
            "</html>";

    public static void main(String args[]) {

        try {
            //todo: fix
            List<PriceRow> rows = null;

//                    PriceParserTwo.getInstance(PRICE_FILE, KNOWN_MODELS_FILE)
//                    .setSheetParams(4, 1, 2, 7,6).getPriceRows();

            Document doc = Jsoup.parse(PAGE);

            Elements elements = doc.select("#price");
            Element el = elements.get(0);
            StringBuffer table = new StringBuffer("<table class=\"table table-striped\">");
            table.append("         <tr>\n" +
                    "                    <th>Модель</th>\n" +
                    "                    <th>Описание</th>\n" +
                    "                    <th>До 400 тыс.руб</th>\n" +
                    "                    <th>Розничная цена</th>\n" +
                    "                </tr>");
//            el.append(tableStart);
            for (PriceRow row : rows) {
                table.append("<tr> <td> Stels " + row.getModelName() + "</td> <td class = \"desc\">" + row.getDescription() +
                        "</td><td>" + row.getPrice400() + "</td><td>" + row.getRetailPrice() + "</td></tr>");

            }
            table.append("</table>");
            el.append(table.toString());

            System.out.println(doc);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


}
