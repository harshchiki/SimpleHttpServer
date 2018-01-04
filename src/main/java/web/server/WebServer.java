package web.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import web.connection.Connection;

public class WebServer extends Thread{
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

		// no point creating the thread pool at this stage
		// the thread pool is created only when the web server is run
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
					// this thread pool internally creates a thread whether or not
					// Connection is a thread or a Runnable
					// so Connection should be a Runnable, to avoid 'Thread' creation cost
					final Socket client = this.serverSocket.accept();
					logger.info("WebServer: Incoming request!!");
					this.threadPool.execute(new Connection(client, this));
				} catch(IOException e) {
					
				}
			}
		} catch (IOException e) {
			String error = "Server shutting down, cannot listen on port: " + this.port;
			logger.error(error);
			throw new RuntimeException(error);
		} finally{
			this.dispose();
			logger.info("Server shut down complete");
			
			
		}
	}

	public void dispose(){
		try {
			if(null != this.serverSocket) {
				this.serverSocket.close();
				logger.info("Server shutting down: Successfully closed server socket listening on port: " + this.port);
			}
		} catch(IOException e) {
			logger.error("Exception occured while closing the server socket, listening on port: " + this.port);
			logger.error("Error Message: " + e.getMessage());
		}
	}
}
