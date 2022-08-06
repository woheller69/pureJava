package foo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ResourceBundle;

public class MainClass {
    public static void main(String[] arg) {
        ResourceBundle bundle = ResourceBundle.getBundle("strings");
        System.out.println("" + bundle.getString("hello"));
        InputStream inputStream = MainClass.class.getResourceAsStream("/files/test.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String line = reader.readLine();
            System.out.println(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
