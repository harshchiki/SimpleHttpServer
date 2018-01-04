package web.connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.apache.log4j.Logger;

import web.request.HTTPRequest;
import web.request.utils.RequestParser;
import web.server.WebServer;

public class Connection implements Runnable {
	private final static Logger logger = Logger.getLogger(Connection.class);
	private final Socket socketClient;
	private final WebServer webServer;
	
	private InputStream in;
	private OutputStream out;
	
	
	
	public Connection(final Socket socket, final WebServer webServer) {
		this.socketClient = socket;
		this.webServer = webServer;
		logger.info("Connection: Connection made and processing request");
	}



	@Override
	public void run(){
		try {
			this.in = this.socketClient.getInputStream();
		} catch (IOException e) {
			final String error = "Error while getting input stream from socket: " + e.getMessage();
			logger.error(error);
		}
		
		try {
			this.out = this.socketClient.getOutputStream();
		} catch (IOException e) {
			final String error = "Error while getting input stream from socket: " + e.getMessage();
			logger.error(error);
		}
		
		// extract request and process
		final HTTPRequest httpRequest = RequestParser.getRequest(in, logger);
		if(null == httpRequest){
			logger.error("Request could not be parsed");
		}
		
		try {
			in.close();
			out.close();
			this.socketClient.close();
		} catch (IOException e) {
			logger.error("Error closing socket resources. Message: " + e.getMessage());
		}
		
	}
}
