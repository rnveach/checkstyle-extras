package com.rnveach.tools.checkstyle.extras.printers;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PrintProperty {

    public static void main(String[] args) throws IOException {
        Properties prop = new Properties();
        try (InputStream input = new FileInputStream(
                "M:\\checkstyleExtraWorkspace\\checkstyle-extras\\src\\test\\resources\\com\\rnveach\\tools\\checkstyle\\extras\\printers\\Input.properties")) {
            prop.load(input);

            printProperties(prop);
        }
    }

    public static void printProperties(Properties prop) {
        for (Object key : prop.keySet()) {
            System.out.println(key + ": " + prop.getProperty(key.toString()));
        }
    }

}
