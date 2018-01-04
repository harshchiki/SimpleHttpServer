package mainapp;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import configuration.ConfigurationReader;
import configuration.WebServerConfiguration;
import web.server.WebServer;

public class WebServerMain {
	public static void main(String[] args) {
		// log4j
		BasicConfigurator.configure();
		final Logger logger = Logger.getLogger(WebServerMain.class);

		logger.info("##########################################################");
		logger.info("Application STARTING");
		logger.info("##########################################################");

		final WebServerConfiguration webConfig = new ConfigurationReader("webserver.config").getWebServerConfiguration();

		final WebServer webServer = new WebServer(webConfig.getPort(),
				webConfig.getNoOfThreads(),
				webConfig.getRootPath());

		webServer.start();

		Runtime.getRuntime().addShutdownHook(new Thread(){
			@Override
			public void run(){
				webServer.dispose();
				logger.info("##########################################################");
				logger.info("Application SHUTTING DOWN");
				logger.info("##########################################################");
			}
		});
	}
}

