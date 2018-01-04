package configuration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class ConfigurationReader {
	private final String configFilePath;
	private final Properties properties = new Properties();
	private static final Logger logger = Logger.getLogger(ConfigurationReader.class);
	
	private final String KEY_PORT = "port";
	private final String KEY_NO_OF_THREADS = "noOfThreads";
	private final String KEY_ROOT_PATH = "rootPath";
	
	
	
	public ConfigurationReader(final String configFilePath){
		this.configFilePath = configFilePath;
		
		try {
			this.properties.load(new FileInputStream(this.configFilePath));
		} catch (FileNotFoundException e) {
			logger.error("Config file not found");
		} catch (IOException e) {
			logger.error("Could not load config properties");
		}
	}

	public WebServerConfiguration getWebServerConfiguration(){
		
		if(!checkPreConditions()){
			logger.error("Cannot boot webserver: port or root path not configured");
			throw new RuntimeException("Cannot boot webserver: port or root path not configured.");
		}
		
		final int port = Integer.valueOf(properties.getProperty(KEY_PORT));
        final String rootPath = properties.getProperty(KEY_ROOT_PATH);
		
		final String noOfThreadsConfigured = properties.getProperty(KEY_NO_OF_THREADS);
		
		logger.info("port configured: " + port);
		logger.info("root path configured: " + rootPath);

		if(null == noOfThreadsConfigured){
			logger.info("no of threads not configured, will default");
			return new WebServerConfiguration(port, rootPath);
		}else{
			final int noOfThreads = Integer.valueOf(noOfThreadsConfigured).intValue();
			logger.info("no of threads configured: " + noOfThreads);
			return new WebServerConfiguration(port, noOfThreads, rootPath);
		}
		
	}
	
	private boolean checkPreConditions(){
		boolean areNecessaryPropertiesProvided = true;
		
		final String port = this.properties.getProperty(KEY_PORT);
		if(null == port || 0 == port.length()){
			areNecessaryPropertiesProvided = false;
		}
		
		final String rootPath = this.properties.getProperty(KEY_ROOT_PATH);
		if(null == rootPath){
			areNecessaryPropertiesProvided = false;
		}
		
		return areNecessaryPropertiesProvided;
	}
}
