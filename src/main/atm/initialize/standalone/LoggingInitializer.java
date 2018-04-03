// Copyright 2001-2013: Thomson Reuters Global Resources: All Rights Reserved. Proprietary and confidential information
// of TRGR. Disclosure, Use or Reproduction without the written authorization of TRGR is prohibited.
package main.atm.initialize.standalone;

import java.io.File;
import java.net.URL;

import org.apache.log4j.xml.DOMConfigurator;

public class LoggingInitializer {

	public static final String LOG4J_CONFIG_FILE_SYSTEM_PROPERTY = "log4jConfigurationFile";
	public static final String LOG_DIRECTORY = "appLogDir";
	public static final String INSTANCE_NAME = "instanceName";

	/**
	 * Configure logging.
	 * 
	 * @return true, if successful
	 */
	public boolean configure() {
		// Check for system properties that may get used as substitution
		// parameters
		// in the log configuration file.
		if (System.getProperty(INSTANCE_NAME) == null) {
			System.err.println("System property " + INSTANCE_NAME
					+ " not set for the instance name, local logging may not work.");
		}
		if (System.getProperty(LOG_DIRECTORY) == null) {
			String userDirLogDirectory = System.getProperty("user.dir") + "/logs";
			try {
				File file = new File(userDirLogDirectory);
				if (file.exists() && file.isDirectory()) {
					System.setProperty(LOG_DIRECTORY, userDirLogDirectory);
					System.out.println("System property " + LOG_DIRECTORY
							+ " not set, defaulting to ${user.dir}/logs, " + userDirLogDirectory);
				} else {
					System.err.println("System property " + LOG_DIRECTORY + " not set, default ${user.dir}/logs, "
							+ userDirLogDirectory + " does not physically exist either, local logging may not work.");
				}
			} catch (Exception e) {
				System.err.println("System property " + LOG_DIRECTORY + " not set, default ${user.dir}/logs, "
						+ userDirLogDirectory + " had an error determining the existence, " + e.toString()
						+ ", local logging may not work.");
			}
		}

		String logConfigFile = System.getProperty(LOG4J_CONFIG_FILE_SYSTEM_PROPERTY);
		// If the log configuration file is set via system property, attempt to
		// use it
		// as a file on the file system first. If it cannot
		// be found, then try to use it as a resource. If that resource does not
		// exist, then default to setting the configuration
		// to the file on the file system
		if (logConfigFile != null) {
			try {
				File file = new File(logConfigFile);
				if (file.exists()) {
					DOMConfigurator.configureAndWatch(logConfigFile);
					System.out.println("Logging initialized via " + logConfigFile);
				} else {
					URL url = System.class.getResource((logConfigFile.startsWith("/") ? "" : "/") + logConfigFile);
					if (url != null) {
						DOMConfigurator.configure(url);
						System.out.println("Logging initialized via " + url.toString());
					} else {
						DOMConfigurator.configureAndWatch(logConfigFile);
						System.out.println("Logging initialized via " + logConfigFile);
					}
				}
			} catch (Exception e) {
				System.err.println("Logging not initialized, Exception=" + e.toString());
				return false;
			}
			return true;
		} else {
			System.err.println("Logging not initialized, System property " + LOG4J_CONFIG_FILE_SYSTEM_PROPERTY
					+ " not set.");
			return false;
		}
	}
}
