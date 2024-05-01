package org.example;

import java.io.FileReader;
import java.io.Reader;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        final String filePath = "src/main/resources/config.properties";
        try (Reader reader = new FileReader(filePath)) {
            Properties properties = new Properties();
            properties.load(reader);
            SomeBean someBean = new Injector().inject(new SomeBean());
            someBean.foo();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}