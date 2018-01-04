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
		if(this.socketClient.isClosed()){
			logger.error("CTOR: Socket Client found closed");
		}

		if(!this.socketClient.isConnected()){
			logger.error("CTOR: Socket client is not connected");
		}
		this.webServer = webServer;
		logger.info("Connection: Connection made and processing request");
	}



	@Override
	public void run(){
		getStreamReferences();

		// 1. Read HTTP request from the client socket's input stream
		final HTTPRequest httpRequest = getRequest(); 
		logger.info("Received request");
		logger.info(httpRequest.toString());
		logger.info("Request prepared");

		if(this.socketClient.isClosed()){
			logger.error("1: Socket Client found closed");
			System.exit(1);
		}

		if(!this.socketClient.isConnected()){
			logger.error("1: Socket client is not connected");
			System.exit(1);
		}

		// 2. Prepare HTTP response 
		HTTPResponse httpResponse = buildHTTPResponse(httpRequest);
		logger.info("Response built");

		if(this.socketClient.isClosed()){
			logger.error("2: Socket Client found closed");
			System.exit(1);
		}

		if(!this.socketClient.isConnected()){
			logger.error("2: Socket client is not connected");
			System.exit(1);
		}

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
		if(this.socketClient == null) {
			logger.error("Socketclient is null");
			return;
		}

		if(!this.socketClient.isConnected()){
			logger.error("Cannot response back, since socket client is already closed");
			return;
		}

		if(this.socketClient.isClosed()){
			logger.error("Socket Client found closed");
			return;
		}




		final StringBuilder bodyBuilder = new StringBuilder();
		for(byte b : httpResponse.getbody()){
			bodyBuilder.append((char)b);
		}
		final String responseString = bodyBuilder.toString();

		logger.info("\n\nWriting the following response to client");
		logger.info(responseString + "\n\n");
//		final PrintWriter writer = new PrintWriter(out);
//		writer.write(httpResponse.toString());
//		writer.flush();
				try {
					this.out.write(httpResponse.getResponseString().getBytes("UTF-8"));
					//			this.out.flush();
				} catch (IOException e) {
					logger.error("Error writing response back to client output stream, because " + e.getMessage());
				}
	}



	private HTTPResponse buildHTTPResponse(final HTTPRequest httpRequest) {
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
