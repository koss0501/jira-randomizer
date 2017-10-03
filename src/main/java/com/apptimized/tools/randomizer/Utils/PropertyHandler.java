package com.apptimized.tools.randomizer.Utils;

import com.apptimized.tools.randomizer.Randomizer.UIInterface.Actions.UIActions;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class PropertyHandler {
    private static Properties props = new Properties();

    public static String getProperty(String propertyName) {
        try {
            props.load(new FileInputStream(new File("config/config.ini")));
            return props.getProperty(propertyName);
        } catch (Exception e) {
            UIActions.showExceptionAlert(e);
            System.exit(0);
        }
        return null;
    }

}
