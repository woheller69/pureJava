package foo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

import org.jsoup.select.Elements;


public class MainClass {
    static String BaseUrl = "https://www.ariva.de/";
    static String appendix ="/kurs";
    public static void main(String[] arg) {
        String isin;
        String place;

        if (arg.length<2) {
            System.out.println("missing arguments -> Test");

            isin ="LU0140636845";
            place ="Frankfurt";
        } else {
            isin = arg[0];
            place = arg[1];
        }

        Document doc;
        String html = BaseUrl+isin+appendix;
        try {

            doc = Jsoup.connect(html).get();
            Elements tableElements = doc.select("table");
/*
            Elements tableHeaderEles = tableElements.select("thead tr th");
            System.out.println("headers");
            for (int i = 0; i < tableHeaderEles.size(); i++) {
                System.out.println(tableHeaderEles.get(i).text());
            }
            System.out.println();
*/
            Elements tableRowElements = tableElements.select(":not(thead) tr");

            for (int i = 0; i < tableRowElements.size(); i++) {
                Element row = tableRowElements.get(i);
                Elements rowItems = row.select("td");
                if (rowItems.size()>0 && rowItems.get(0).text().equals(place)) {
                    System.out.println(place+": "+rowItems.get(1).text().replaceAll("[^0-9,]", ""));
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
