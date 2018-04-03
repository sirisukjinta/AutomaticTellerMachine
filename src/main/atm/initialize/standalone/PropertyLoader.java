// Copyright 2001-2016: Thomson Reuters Global Resources: All Rights Reserved. Proprietary and confidential information
// of TRGR. Disclosure, Use or Reproduction without the written authorization of TRGR is prohibited.
package main.atm.initialize.standalone;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class PropertyLoader {

	public static final String APP_VERSION_NUMBER_KEY = "application.version.number";
	public static final String APP_NAME_KEY = "application.name";
	private static final String PROPERTY_FILE_SYSTEM_PROPERTY = "applicationProperties";
	private static final String JAVA_COMMAND_KEY = "sun.java.command";
	private static final String APP_VERSION_DEFAULT = "0.0.0";

	/**
	 * Do the actual work to load up the properties into system properties.
	 *
	 * @param overridePropertyFileLocation
	 *            The override property file location
	 * @return boolean
	 */
	protected boolean actualLoad(String overridePropertyFileLocation) {
		String propertyFile = System.getProperty(PROPERTY_FILE_SYSTEM_PROPERTY);

		// An override was specified, attempt to use that.
		if (overridePropertyFileLocation != null) {
			propertyFile = overridePropertyFileLocation;
			System.out.println("Property file location is being overridden from " + overridePropertyFileLocation);
		}

		// If the property file location is set via the system property or the
		// override, attempt to first access it via
		// a file on the file system. If it is not there, then attempt to access
		// it via loading it as a resource. If it still
		// is not found at that point, then return false as the properties could
		// not be loaded as if it were set, then the
		// intention was to use it.
		if (propertyFile != null) {
			try {
				return this.standardLoadProcess(propertyFile);
			} catch (Exception e) {
				System.err.println("Properties not loaded, error reading " + propertyFile + "  Exception=" + e.toString());
				return false;
			}
		} else {
			System.err.println("Properties not loaded, System property " + PROPERTY_FILE_SYSTEM_PROPERTY + " is not set.");
			return false;
		}
	}

	/**
	 * Load up the properties into system properties.
	 *
	 * @return boolean
	 */
	public boolean load() {
		return load(null);
	}

	/**
	 * Load up the properties into system properties.
	 *
	 * @param overridePropertyFileLocation
	 *            The override property file location
	 * @return boolean
	 */
	public boolean load(String overridePropertyFileLocation) {
		boolean loaded = this.actualLoad(overridePropertyFileLocation);

		if (loaded) {
			this.loadManifestInfo();
		}

		return loaded;
	}

	/**
	 * Load information from the manifest file. Default values will be loaded if the value doesn't exist or there is an
	 * error.
	 */
	protected void loadManifestInfo() {
		// attempt to get the name of the main class. This is used to get the Manifest file from its jar.
		String mainClassname = System.getProperty(JAVA_COMMAND_KEY);
		if (mainClassname != null) {
			int endIndex = mainClassname.indexOf(" ");
			if (endIndex > 0) {
				mainClassname = mainClassname.substring(0, endIndex);
			}
		} else {
			System.out.println(
					"Couldn't determine the main class name therefore couldn't load the manifest file.  Setting defaults instead.");
			System.getProperties().setProperty(APP_VERSION_NUMBER_KEY, APP_VERSION_DEFAULT);
			return;
		}

		// attempt to read the manifest file, specifically for the application version and name.
		try {
			// get the main class
			Class<?> klass = Class.forName(mainClassname);
			// find the path to the jar it's in
			String jarPath = klass.getResource("/" + mainClassname.replace('.', '/') + ".class").getPath();
			if (jarPath != null) {
				int endIndex = jarPath.indexOf('!');
				if (endIndex > 0) {
					jarPath = jarPath.substring(0, endIndex);
				}
			}

			// get the MANIFEST.MF from the jar
			try (JarFile jarFile = new JarFile(new File(new URI(jarPath)), false)) {
				Manifest manifest = jarFile.getManifest();
				Attributes mainAttributes = manifest.getMainAttributes();

				String version = mainAttributes.getValue(Attributes.Name.IMPLEMENTATION_VERSION);
				if (version == null) {
					version = APP_VERSION_DEFAULT;
				}
				System.getProperties().setProperty(APP_VERSION_NUMBER_KEY, version);

				String appName = mainAttributes.getValue(Attributes.Name.IMPLEMENTATION_TITLE);
				if (appName != null) {
					System.getProperties().setProperty(APP_NAME_KEY, appName);
				}
			}
		} catch (IOException | ClassNotFoundException | URISyntaxException | IllegalArgumentException ex) {
			System.err.println("An Exception occured while trying to load the manifest file into a properties "
					+ "object.  Some default may be used instead.");
			System.getProperties().setProperty(APP_VERSION_NUMBER_KEY, APP_VERSION_DEFAULT);
		}
	}

	/**
	 * Standard load process.
	 *
	 * @param propertyFileName
	 *            the property file name
	 * @return true, if successful
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	protected boolean standardLoadProcess(String propertyFileName) throws IOException {
		File file = new File(propertyFileName);

		if (file.exists()) {
			FileInputStream fis = new FileInputStream(propertyFileName);
			System.getProperties().load(fis);
			fis.close();
			System.out.println("Properties loaded via " + propertyFileName);
			return true;
		}

		URL url = System.class.getResource((propertyFileName.startsWith("/") ? "" : "/") + propertyFileName);

		if (url != null) {
			InputStream is = url.openStream();
			System.getProperties().load(is);
			is.close();
			System.out.println("Properties loaded via " + url.toString());
			return true;
		}

		System.err.println("Properties not loaded, error reading " + propertyFileName + ".  It is not on the "
				+ "local filesystem or as a resource.");
		return false;
	}
}
