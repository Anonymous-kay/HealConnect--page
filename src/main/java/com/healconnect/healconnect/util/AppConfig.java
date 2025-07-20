package com.healconnect.healconnect.util; // Corrected package

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppConfig {
    private static final Properties props = new Properties();

    static {
        try (InputStream input = AppConfig.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.err.println("Unable to find config.properties");
            } else {
                props.load(input);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static String getUploadDir() {
        return props.getProperty("file.upload.directory", "uploads");
    }

    public static int getMaxFileSizeMB() {
        return Integer.parseInt(props.getProperty("max.file.size", "10"));
    }
}
