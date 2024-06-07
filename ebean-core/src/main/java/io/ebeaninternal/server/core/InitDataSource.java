package io.ebeaninternal.server.core;

import io.ebean.annotation.Platform;
import io.ebean.DatabaseBuilder;
import io.ebean.datasource.*;

import javax.persistence.PersistenceException;
import javax.sql.DataSource;

/**
 * Initialise the main DataSource and read-only DataSource.
 */
final class InitDataSource {

  private final DatabaseBuilder.Settings config;

  /**
   * Create and set the main DataSource and read-only DataSource.
   */
  static void init(DatabaseBuilder.Settings config) {
    new InitDataSource(config).initialise();
  }

  InitDataSource(DatabaseBuilder.Settings config) {
    this.config = config;
  }

  private void initialise() {
    if (config.getDataSource() == null) {
      config.setDataSource(initDataSource());
    }
    if (config.getReadOnlyDataSource() == null) {
      config.setReadOnlyDataSource(initReadOnlyDataSource());
    }
  }

  /**
   * Initialise the "main" read write DataSource from configuration.
   */
  private DataSource initDataSource() {
    return createFromConfig(config.getDataSourceConfig(), config.readOnlyDatabase(), false);
  }

  /**
   * Initialise the "read only" DataSource from configuration.
   */
  private DataSource initReadOnlyDataSource() {
    if (config.readOnlyDatabase()) {
      // using the same DataSource instance
      return config.getDataSource();
    }
    var roConfig = readOnlyConfig();
    return roConfig == null ? null : createFromConfig(roConfig, false, true);
  }

  DataSourceBuilder.Settings readOnlyConfig() {
    var roConfig = config.getReadOnlyDataSourceConfig();
    if (roConfig == null) {
      // it has explicitly been set to null, not expected but ok
      return null;
    }
    if (urlSet(roConfig.getUrl())) {
      return roConfig;
    }
    // convenient alternate place to set the read-only url
    final String readOnlyUrl = config.getDataSourceConfig().getReadOnlyUrl();
    if (urlSet(readOnlyUrl)) {
      roConfig.url(readOnlyUrl);
      return roConfig;
    }
    if (config.isAutoReadOnlyDataSource()) {
      roConfig.url(null); // blank out in case it is "none"
      return roConfig;
    } else {
      return null;
    }
  }

  private boolean urlSet(String url) {
    return url != null && !"none".equalsIgnoreCase(url) && !url.trim().isEmpty();
  }

  private DataSource createFromConfig(DataSourceBuilder.Settings dsConfig, boolean readOnlyDB, boolean readOnly) {
    if (dsConfig == null) {
      throw new PersistenceException("No  DataSourceBuilder defined for " + config.getName());
    }
    if (dsConfig.isOffline() && config.getDatabasePlatformName() == null) {
      throw new PersistenceException("You MUST specify a DatabasePlatformName on DatabaseConfig when offline");
    }

    attachAlert(dsConfig);
    attachListener(dsConfig);

    if (readOnlyDB) {
      dsConfig.autoCommit(true);
      dsConfig.readOnly(true);
    } else if (readOnly) {
      // setup to use AutoCommit such that we skip explicit commit
      var mainSettings = config.getDataSourceConfig();
      dsConfig.autoCommit(true);
      dsConfig.readOnly(true);
      dsConfig.setDefaults(mainSettings);
      dsConfig.isolationLevel(mainSettings.getIsolationLevel());
    } else if (isPostgresAllQuotedIdentifiers()) {
      dsConfig.addProperty("quoteReturningIdentifiers", false);
    }
    return create(dsConfig, readOnly);
  }

  boolean isPostgresAllQuotedIdentifiers() {
    return config.isAllQuotedIdentifiers() && Platform.POSTGRES == config.getDatabasePlatform().platform().base();
  }

  private DataSource create(DataSourceBuilder dsConfig, boolean readOnly) {
    String poolName = config.getName() + (readOnly ? "-ro" : "");
    return dsConfig.name(poolName).build();
  }

  /**
   * Attach DataSourceAlert via service loader if present.
   */
  private void attachAlert(DataSourceBuilder.Settings dsConfig) {
    DataSourceAlertFactory alertFactory = config.getServiceObject(DataSourceAlertFactory.class);
    if (alertFactory == null) {
      alertFactory = ServiceUtil.service(DataSourceAlertFactory.class);
    }
    if (alertFactory != null) {
      dsConfig.alert(alertFactory.createAlert());
    }
  }

  /**
   * Create and attach a DataSourcePoolListener if it has been specified via properties and there is not one already attached.
   */
  private void attachListener(DataSourceBuilder.Settings dsConfig) {
    if (dsConfig.getListener() == null) {
      String poolListener = dsConfig.getPoolListener();
      if (poolListener != null) {
        dsConfig.listener((DataSourcePoolListener) config.getClassLoadConfig().newInstance(poolListener));
      }
    }
  }
}
