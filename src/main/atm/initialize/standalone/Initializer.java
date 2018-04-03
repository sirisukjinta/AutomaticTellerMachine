// Copyright 2001-2013: Thomson Reuters Global Resources: All Rights Reserved. Proprietary and confidential information
// of TRGR. Disclosure, Use or Reproduction without the written authorization of TRGR is prohibited.
package main.atm.initialize.standalone;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Initializer {

	public static final String HOST_SERVER_NAME = "dco.server.name";

	/**
	 * Instantiates a new Initializer object.
	 */
	public Initializer() {
	}

	/**
	 * Bootstraps this process with infrastructure items. E.g. Logging, Properties, Spring Context, etc.
	 */
	public void setup() {
		// Load the properties.
		PropertyLoader propertyLoader = new PropertyLoader();
		boolean loaded = propertyLoader.load();
		if (!loaded) {
			System.err.println("The properties were not able to be loaded.  The app is going to end. "
					+ "Properties are too critical for the application to function without them being loaded.");
			System.exit(1);
			return;
		}

		// try to get the name of the current host and put it into the System properties
		String computername = this.getComputerName();
		System.setProperty(Initializer.HOST_SERVER_NAME, computername);

		// configure logging
		LoggingInitializer loggingInitializer = new LoggingInitializer();
		loggingInitializer.configure();
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
