package foo;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.jsoup.select.Elements;

public class MainClass {
    static String BaseUrlAriva = "https://www.ariva.de/";
    static String appendixAriva ="/kurs";
    static String BaseUrlTradegate = "https://www.tradegate.de/refresh.php?isin=";

    public static void main(String[] arg) {
        String isin;
        String place;
        String name;

        if (arg.length < 3) {
            System.out.println("missing arguments -> Test");

            isin ="DE000A0XFSF0";
            place ="Tradegate";
            name = "Demire";
        } else {
            isin = arg[0];
            place = arg[1];
            name = arg[2];
        }

        if (place.equals("Tradegate")){
            try {
                // Construct the URL for Tradegate API
                String apiUrl = BaseUrlTradegate + isin;
                // Create URL object and open connection
                URL url = new URL(apiUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");

                // Check if request was successful
                int responseCode = conn.getResponseCode();
                if (responseCode == 200) {
                    // Read the response
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(conn.getInputStream())
                    );
                    StringBuilder response = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                        //System.out.println(line);
                    }
                    reader.close();

                    // Parse JSON response
                    JSONObject jsonObject = new JSONObject(response.toString());

                    Object lastValue = jsonObject.get("last");
                    if (lastValue instanceof  String && lastValue.equals("./.")){
                        lastValue = jsonObject.get("close");
                    }
                    String lastPrice;

                    if (lastValue instanceof String) {
                        lastPrice = (String) lastValue;
                    } else if (lastValue instanceof Number) {
                        // Convert number to string with comma as decimal separator
                        double value = ((Number) lastValue).doubleValue();
                        lastPrice = String.format("%.3f", value).replace('.', ',');
                    } else {
                        lastPrice = lastValue.toString();
                    }

                    //System.out.println(isin+"\t"+lastPrice+"\t"+name);
                    System.out.println(lastPrice);
                } else {
                    System.out.println("Error");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Document doc;
            String apiUrl = BaseUrlAriva +isin+ appendixAriva;
            try {

                doc = Jsoup.connect(apiUrl).get();
                Elements tableElements = doc.select("table");
                Elements tableRowElements = tableElements.select(":not(thead) tr");

                for (int i = 0; i < tableRowElements.size(); i++) {
                    Element row = tableRowElements.get(i);
                    Elements rowItems = row.select("td");
                    if (rowItems.size()>0 && rowItems.get(0).text().equals(place)) {
                        //System.out.println(isin+"\t"+rowItems.get(1).text().replaceAll("[^0-9,]", "")+"\t"+name);
                        System.out.println(rowItems.get(1).text().replaceAll("[^0-9,]", ""));
                        break;
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
