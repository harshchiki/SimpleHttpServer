package web.connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.apache.log4j.Logger;

import web.request.HTTPRequest;
import web.request.utils.RequestParser;
import web.response.HTTPResponse;
import web.response.impl.BadRequestResponse;
import web.response.impl.DELETEResponse;
import web.response.impl.FileResponse;
import web.response.impl.OPTIONSReponse;
import web.response.impl.POSTResponse;
import web.response.impl.PUTResponse;
import web.server.WebServer;

/*
 * Read more: http://javarevisited.blogspot.com/2015/06/how-to-create-http-server-in-java-serversocket-example.html#ixzz53Bsp5etb
 * 
 * 1. Read HTTP request from the client socket 
 * 2. Prepare HTTP response 
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
		logger.info("Connection made and processing request");
	}

	@Override
	public void run(){
		getStreamReferences();

		// 1. Read HTTP request from the client socket's input stream
		final HTTPRequest httpRequest = getRequest(); 
		logger.info("Received request");
		logger.info(httpRequest.toString());
		logger.info("Request prepared");

		// 2. Prepare HTTP response 
		HTTPResponse httpResponse = null;
		switch(httpRequest.getHTTPMethod()){
		case GET:
			httpResponse = buildFileResponse(httpRequest);
			break;
		case POST:
			httpResponse = buildPOSTResponse(httpRequest);
			break;
		case PUT:
			httpResponse = buildPUTResponse(httpRequest);
			break;
		case DELETE:
			httpResponse = buildDELETEResponse(httpRequest);
			break;
		case OPTIONS:
			httpResponse = buildOPTIONSResponse(httpRequest);
			break;
		}
		logger.info("Response built");


		// 3. Send HTTP response to the client
		respond(httpResponse);
		logger.info("Response sent to client");

		// 4. Close the socket and other resources
		closeIOResources();
		logger.info("Resources closed/disposed");

	}



	private void closeIOResources() {
		try {
			if(this.socketClient != null && this.socketClient.isConnected()){
				in.close();
				out.close();
				this.socketClient.close();
			}else { 
				logger.error("Socket client found closed, before trying to close");
			}

		} catch (IOException e) {
			logger.error("Error closing socket resources. Message: " + e.getMessage());
		}
	}



	private void respond(final HTTPResponse httpResponse) {
		if(null == httpResponse) {
			return;
		}

		logger.info("\n\nWriting response to client");
		try {
			this.out.write(httpResponse.getResponseString().getBytes("UTF-8"));
		} catch (IOException e) {
			logger.error("Error writing response back to client output stream, because " + e.getMessage());
		}
	}


	private HTTPResponse buildFileResponse(final HTTPRequest httpRequest) {
		return new FileResponse(this.webServer.getRootPath(), httpRequest.getURL());
	}
	
	
	private HTTPResponse buildPOSTResponse(final HTTPRequest httpRequest) {
		return new POSTResponse();
	}
	
	private HTTPResponse buildPUTResponse(final HTTPRequest httpRequest) {
		return new PUTResponse();
	}
	
	private HTTPResponse buildDELETEResponse(final HTTPRequest httpRequest) {
		return new DELETEResponse();
	}
	
	private HTTPResponse buildOPTIONSResponse(final HTTPRequest httpRequest) {
		return new OPTIONSReponse();
	}



	private HTTPRequest getRequest() {
		final HTTPRequest httpRequest = RequestParser.getRequest(in, logger);
		if(null == httpRequest){							
			logger.error("Request could not be parsed");
			// 2. Prepare an HTTP response for BAD REQUEST (400)
			respond(new BadRequestResponse());
			throw new RuntimeException("Bad request");
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
