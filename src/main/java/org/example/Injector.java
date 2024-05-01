package org.example;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

public class Injector {

    public <T> T inject(T object) throws Exception {
        Properties properties = new Properties();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (inputStream != null) {
                properties.load(inputStream);

                Field[] fields = object.getClass().getDeclaredFields();
                for (Field field : fields) {
                    if (field.isAnnotationPresent(AutoInjectable.class)) {
                        Class<?> type = field.getType();
                        Object impl = Class.forName(properties.getProperty(type.getName())).newInstance();
                        field.setAccessible(true);
                        field.set(object, impl);
                    }
                }
            } else {
                throw new IOException("File not found: config.properties");
            }
        } catch (IOException e) {
            System.out.println("Error loading properties file: " + e.getMessage());
        }

        return object;
    }


    private static List<Field> getFieldsWithAnnotation(Object obj, Class<? extends Annotation> annotation) {
        List<Field> result = new ArrayList<Field>();
        Class<?> class1 = obj.getClass();

        while (class1 != null)
        {
            for (Field field : class1.getDeclaredFields())
                if (field.isAnnotationPresent(annotation))
                    result.add(field);

            class1 = class1.getSuperclass();
        }

        return result;
    }
    private String getClassNameFromProperties(String interfaceName) {
        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            properties.load(input);
            return properties.getProperty(interfaceName);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error loading properties file: "+ e.getMessage());
        }
    }

}