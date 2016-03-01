package com.xetus.pci.wake.manager

import javax.inject.Inject

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.plugin.core.PluginRegistry
import org.springframework.stereotype.Component


/**
 * A service for issuing queries against any log managers for which a 
 * LogManagerClientPlugin has been added to the classpath.
 */
@Slf4j
@Component
@CompileStatic
class LogManagerQueryService {
  
  @Inject
  @Qualifier("logManagerClientPluginRegistry")
  PluginRegistry<LogManagerClientPlugin<? extends LogManagerClientConfig>, ? extends LogManagerClientConfig> clientRegistry
  
  /**
   * Issues a query generated by combining the supplied {@link 
   * LogManagerQueryConfig}, <code>startTime</code> and <code>endTime</code>
   * to the log manager configured in the supplied {@link 
   * LogManagerQueryConfig}'s {@link LogManagerConnectionConfig} instance. 
   * 
   * Note that this method use the supplied {@link LogManagerConfig}'s type
   * to determine the applicable {@link LogManagerClientPlugin} implementation
   * to use.
   * 
   * @param config
   * @param startTime
   * @param endTime
   * 
   * @return the {@link LogQueryResult} instance returned by the applicable
   * {@link LogManagerClientPlugin} implementation's {@link 
   * LogManagerClientPlugin#getLogs(LogManagerConfig, java.util.Date, 
   * java.util.Date)}
   * method.
   */
  public LogQueryResult query(LogManagerClientConfig config, 
                              Date startTime, 
                              Date endTime) {
    LogManagerClientPlugin client = clientRegistry.getPluginFor(config)
    if (client == null) {
      throw new IllegalArgumentException("Failed to locate "
        + "LogManagerClientPlugin applicable for type: "
        + config.class.name + "; available plugins: "
        + clientRegistry.plugins?.collect { it.class.name })
    }
    return client.getLogs(config, startTime, endTime)
  }

}
