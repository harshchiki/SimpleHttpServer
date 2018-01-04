package web.request.impl;

import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

import web.request.enums.HTTPMethod;

public abstract class HTTPRequestBase {

	protected void logRequestAttributes(final String URL,
			final String protocol,
			final Map<String, String> headers,
			final List<String> body,
			final HTTPMethod httpMethod,
			final Logger logger){
		final StringBuilder builder = new StringBuilder();
		builder.append("\n\n\n***************************************************************************");
		builder.append("\nHTTP request: "+httpMethod);
		builder.append("\nURL: " + URL);
		builder.append("\nProtocol: " + protocol);
		builder.append("\nHeader:");
		headers.entrySet().stream().forEach(entry -> 
			builder.append("\n" + entry.getKey() + ": " + entry.getValue())
		);
		builder.append("\nBody:");
		body.stream().forEach(line ->  builder.append(line));
		builder.append("\n***************************************************************************");
		logger.info(builder.toString());
		
		
	}
}
