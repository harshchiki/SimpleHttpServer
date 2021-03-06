package configuration;

public class WebServerConfiguration {
	private int port;
	private int noOfThreads = 10; // default value is set to 10
	private String rootPath;
	
	WebServerConfiguration(final int port, final int noOfThreads, final String rootPath) {
		super();
		this.port = port;
		this.noOfThreads = noOfThreads;
		this.rootPath = rootPath;
	}
	
	public int getPort() {
		return port;
	}

	public int getNoOfThreads() {
		return noOfThreads;
	}

	public String getRootPath() {
		return rootPath;
	}

	WebServerConfiguration(final int port, final String rootPath) {
		super();
		this.port = port;
		this.rootPath = rootPath;
	}
	
	@Override
	public String toString() {
		final StringBuilder strbuilder = new StringBuilder();
		
		strbuilder.append("port: " + this.port + "\n");
		strbuilder.append("number of threads: " + this.noOfThreads + "\n");
		strbuilder.append("root path: " + this.rootPath + "\n");
		
		return strbuilder.toString();
	}
}
