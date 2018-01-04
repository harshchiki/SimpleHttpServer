package web.connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import org.apache.log4j.Logger;

import web.request.HTTPRequest;
import web.request.utils.RequestParser;
import web.response.HTTPResponse;
import web.response.impl.BadRequestResponse;
import web.response.impl.FileResponse;
import web.server.WebServer;

/*
 * Read more: http://javarevisited.blogspot.com/2015/06/how-to-create-http-server-in-java-serversocket-example.html#ixzz53Bsp5etb
 * 
 * 1. Read HTTP request from the client socket 
 * 2. Prepare an HTTP response 
 * 3. Send HTTP response to the client 
 * 4. Close the socket
 */
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
		getStreamReferences();
		
		// 1. Read HTTP request from the client socket's input stream
		final HTTPRequest httpRequest = getRequest(); 
		
		// 2. Prepare an HTTP response 
		HTTPResponse httpResponse = buildResponse(httpRequest);
		
		
		// 3. Send HTTP response to the client
		respond(httpResponse);
		
		// 4. Close the socket and other resources
		closeIOResources();
		
	}



	private void closeIOResources() {
		try {
			in.close();
			out.close();
			this.socketClient.close();
		} catch (IOException e) {
			logger.error("Error closing socket resources. Message: " + e.getMessage());
		}
	}



	private void respond(final HTTPResponse httpResponse) {
		final String responseString  = httpResponse.toString();
		final PrintWriter writer = new PrintWriter(out);
		writer.write(responseString);
		writer.flush();
	}



	private HTTPResponse buildResponse(final HTTPRequest httpRequest) {
		HTTPResponse httpResponse;
		httpResponse = new FileResponse(this.webServer.getRootPath(), httpRequest.getURL());
		httpResponse.buildResponse();
		return httpResponse;
	}



	private HTTPRequest getRequest() {
		final HTTPRequest httpRequest = RequestParser.getRequest(in, logger);
		HTTPResponse httpResponse = null;
		if(null == httpRequest){
			logger.error("Request could not be parsed");
			// 2. Prepare an HTTP response for BAD REQUEST (400)
			httpResponse = new BadRequestResponse();
			httpResponse.buildResponse();
		}
		return httpRequest;
	}



	private void getStreamReferences() {
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
	}
}
