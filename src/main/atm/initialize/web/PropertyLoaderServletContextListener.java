// Copyright 2001-2015: Thomson Reuters Global Resources: All Rights Reserved. Proprietary and Confidential information of TRGR.
// Disclosure, Use or Reproduction without the written authorization of TRGR is prohibited.
package main.atm.initialize.web;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import main.atm.initialize.standalone.Initializer;

import com.trgr.cobalt.infrastructure.logging.PropertyLoader;

/**
 * A class that will be used to load the properties. If the initialize fails, then System.exit will be called as the
 * loading of the properties is too critical for the application to function to have it fail.
 * 
 */
public class PropertyLoaderServletContextListener implements ServletContextListener {

	public static final String APP_VERSION_NUMBER_KEY = "application.version.number";
	private static final String MANIFEST_FILE_NAME = "/META-INF/MANIFEST.MF";
	private static final String MANIFEST_APP_VERSION_KEY = "Implementation-Version";
	private static final String APP_VERSION_DEFAULT = "0.0.0";

	/**
	 * Destroy the context
	 * 
	 * @param servletContextEvent
	 */
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
	}

	/**
	 * Initialize the context. If the loading of the properties fails, System.exit will be called.
	 * 
	 * @param servletContextEvent
	 */
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		String overridePropertyFileLocation = null;
		if (servletContextEvent != null) {
			ServletContext sc = servletContextEvent.getServletContext();
			if (sc != null) {
				overridePropertyFileLocation = sc.getInitParameter("overridePropertyFileLocation");
				if (overridePropertyFileLocation != null) {
					System.out.println("PropertyLoaderServletContextListener: overridePropertyFileLocation="
							+ overridePropertyFileLocation);
				}
			}
		}

		boolean loaded = PropertyLoader.load(overridePropertyFileLocation);
		if (!loaded) {
			System.err
					.println("The properties were not able to be loaded, the app server is going to end.  Properties are too critical for the "
							+ "application to function without them being loaded.");
			System.exit(1);
		} else {
			loadManifestInfo(servletContextEvent.getServletContext());
			System.setProperty(Initializer.HOST_SERVER_NAME, getComputerName());
		}
	}

	/**
	 * Load information from the manifest file. Default values will be loaded if the value doesn't exist or there is an
	 * error.
	 * 
	 * @param servletContext
	 *            the servlet context
	 */
	protected void loadManifestInfo(ServletContext servletContext) {
		Properties tempProps = new Properties();
		InputStream inStream = servletContext.getResourceAsStream(PropertyLoaderServletContextListener.MANIFEST_FILE_NAME);

		try {
			tempProps.load(inStream);
			String version = tempProps.getProperty(PropertyLoaderServletContextListener.MANIFEST_APP_VERSION_KEY,
					PropertyLoaderServletContextListener.APP_VERSION_DEFAULT);
			System.setProperty(PropertyLoaderServletContextListener.APP_VERSION_NUMBER_KEY, version);
		} catch (IOException ex) {
			System.err.println("An IOException occured while trying to load the manifest file into a properties "
					+ "object.  Setting defaults instead.");
			System.setProperty(PropertyLoaderServletContextListener.APP_VERSION_NUMBER_KEY,
					PropertyLoaderServletContextListener.APP_VERSION_DEFAULT);
		}
	}

	/**
	 * Gets the computer name.
	 * 
	 * @return the computer name
	 */
	protected String getComputerName() {
		String computerName = "";
		try {
			computerName = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e1) {
			System.out.println("Not able to obtain host name.");
			computerName = "Unknown";
		}
		return computerName;
	}
}