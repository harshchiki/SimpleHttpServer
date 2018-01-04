package web.request.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import web.request.HTTPRequest;
import web.request.enums.HTTPMethod;
import web.request.impl.DELETERequest;
import web.request.impl.GETRequest;
import web.request.impl.OPTIONSRequest;
import web.request.impl.POSTRequest;
import web.request.impl.PUTRequest;

public class RequestParser {
	public static HTTPRequest getRequest(final InputStream in, final Logger logger){
		HTTPRequest httpRequest = null;
		
		final BufferedReader reader = getReader(in);
		try {
			// read the first line in request for HTTPMethod
			String line = reader.readLine();
			if(null == line){
				final String errorMsg = "Invalid request: Insufficient information";
				logger.error(errorMsg);
				throw new RuntimeException(errorMsg);
			}
			
			final String[] request = line.split(" ", 3);
			if(3 != request.length){
				final String errorMsg = "Cannot parse request: Insufficient information.";
				logger.error(errorMsg);
				throw new RuntimeException(errorMsg);
			}
			
			if(!request[2].startsWith("HTTP/")) {
				final String errorMsg = "Invalid request: Not an HTTP request";
				logger.error(errorMsg);
				throw new RuntimeException(errorMsg);
			}
			
			
			final HTTPMethod httpMethod = getMethod(request[0]);
			final String URL = request[1];
			final String protocol = request[2];
			
			
			// read the second line in request for HEADERS
			line = reader.readLine();
			final Map<String, String> headers = new HashMap<>();
			while(null != null && !line.equals("")) {
				final String[] headersInLine = line.split(": ", 2);
				if(2 != headersInLine.length) {
					// need to add more info for debugging
					logger.error("Cannot parse headers from the request");
					throw new RuntimeException("Cannot parse headers");
				} else { 
					headers.put(headersInLine[0], headersInLine[1]);
				}
				line = reader.readLine();
			}
			
			// read the following lines for the body of the request
			final List<String> bodyOfRequest = new LinkedList<>();
			while(reader.ready()) {
				line = reader.readLine();
				bodyOfRequest.add(line);
			}
			
			// now construct the HTTPRequest instance
			switch(httpMethod){
			case GET:
				httpRequest = new GETRequest(URL, protocol, headers, bodyOfRequest);
				break;
			case POST:
				httpRequest = new POSTRequest(URL, protocol, headers, bodyOfRequest);
				break;
			case PUT:
				httpRequest = new PUTRequest(URL, protocol, headers, bodyOfRequest);
				break;
			case DELETE:
				httpRequest = new DELETERequest(URL, protocol, headers, bodyOfRequest);
				break;
			case OPTIONS:
				httpRequest = new OPTIONSRequest(URL, protocol, headers, bodyOfRequest);
				break;
			}
		} catch (IOException e) {
			final String error = "Cannot parse request: Error reading request stream. Message: " + e.getMessage();
			logger.error(error);
			throw new RuntimeException(error);
		} 
		
		return httpRequest;
	}
	
	private static HTTPMethod getMethod(final String token){
		switch(token.trim().toLowerCase()){
		case "get":
			return HTTPMethod.GET;
		case "options":
			return HTTPMethod.OPTIONS;
		case "delete":
			return HTTPMethod.DELETE;
		case "put":
			return HTTPMethod.PUT;
		case "post":
			return HTTPMethod.POST;
		default:
			throw new RuntimeException("Invalid request: Invalid method " + token.trim());
		}
	}
	private static BufferedReader getReader(final InputStream in) {
		return new BufferedReader(new InputStreamReader(in));
	}
}
