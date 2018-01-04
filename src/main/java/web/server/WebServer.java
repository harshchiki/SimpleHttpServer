package web.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import web.connection.Connection;

public class WebServer extends Thread {
	private final static Logger logger = Logger.getLogger(WebServer.class);
	private final int port;
	private final String rootPath;
	

	private final int noOfThreads;
	private ServerSocket serverSocket;

	private Executor threadPool;

	public WebServer(final int port,
			final int noOfThreads,
			final String rootPath){
		this.port = port;
		this.rootPath = rootPath;
		this.noOfThreads = noOfThreads;
	}

	@Override
	public void run(){
		try {
			logger.info("Server started: Listening on port: " + this.port
					+ " root path: " + this.rootPath
					+ " no. of threads: " + this.noOfThreads);
			this.serverSocket = new ServerSocket(this.port);
			
			logger.info("Listening on port: " + this.port);
			this.threadPool = Executors.newFixedThreadPool(this.noOfThreads);
			
			while(!Thread.interrupted()){
				try {
					final Socket client = this.serverSocket.accept();
					logger.info("WebServer: Incoming request!!");
					this.threadPool.execute(new Connection(client, this));
				} catch(IOException e) {
					logger.error("Error listening on " + this.port + " because " + e.getMessage());
				}
			}
		} catch (IOException e) {
			String error = "Server shutting down, cannot listen on port: " + this.port;
			logger.error(error);
		} 
	}

	public void dispose(){
		logger.info("dispose called");
		try {
			if(null != this.serverSocket && !this.serverSocket.isClosed()) {
				this.serverSocket.close();
				logger.info("Server shutting down: Successfully closed server socket listening on port: " + this.port);
			}
			
		} catch(IOException e) {
			logger.error("Exception occured while closing the server socket, listening on port: " + this.port);
			logger.error("Error Message: " + e.getMessage());
		}
	}
	
	public String getRootPath() {
		return rootPath;
	}
}
