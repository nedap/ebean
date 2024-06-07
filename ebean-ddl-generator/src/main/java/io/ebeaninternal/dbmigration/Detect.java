package io.ebeaninternal.dbmigration;

import io.ebean.DatabaseBuilder;

/**
 * Detect existence of JAXB
 */
public class Detect {

  /**
   * Return true if JAXB is present.
   */
  public static boolean isJAXBPresent(DatabaseBuilder.Settings config) {
    return config.getClassLoadConfig().isPresent("javax.xml.bind.JAXBException");
  }
}
