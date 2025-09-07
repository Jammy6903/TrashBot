package com.jami.bot;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;

public class Props {

  private static final Logger LOGGER = Log.getLogger();

  private static final File PROP_FILE = new File("config.properties");
  private static Properties props = new Properties();

  public static void loadProperties() {
    
    LOGGER.info("[INFO] Loading properties file");
    try {
      props.load(new FileInputStream(PROP_FILE));
    } catch (IOException e) {
      LOGGER.error("[ERROR] Property file not found at {}", PROP_FILE, e);
      System.exit(1);
    }
    LOGGER.info("[INFO] Properties loaded");
  }

  public static Properties getProps() {
    return props;
  }

  public static void saveProps() {
    try {
      props.store(new FileOutputStream(PROP_FILE), null);
    } catch (Exception e) {
      LOGGER.error("[ERROR] Unable to save to properties file - ", e);
    }
  }
}
