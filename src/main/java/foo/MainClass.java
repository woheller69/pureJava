package foo;

import java.util.ResourceBundle;

public class MainClass {
    public static void main(String[] arg) {
        ResourceBundle bundle = ResourceBundle.getBundle("strings");
        System.out.println("" + bundle.getString("hello"));

    }
}
