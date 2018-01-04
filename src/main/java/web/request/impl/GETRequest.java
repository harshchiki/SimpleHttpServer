package web.request.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import web.request.HTTPRequest;
import web.request.enums.HTTPMethod;

// TO-DO: implement toString()
public class GETRequest extends HTTPRequestBase implements HTTPRequest {
	private final String url;
	private final String protocol;
	private final Map<String, String> headers;
	private final List<String> body;
	
	private static final Logger logger = Logger.getLogger(GETRequest.class);
	
	public GETRequest(String url, String protocol, Map<String, String> headers, List<String> body) {
		this.url = url;
		this.protocol = protocol;
		this.headers = headers;
		this.body = body;
		
		logRequestAttributes(this.url, protocol, headers, body, HTTPMethod.GET, logger);
	}

	@Override
	public HTTPMethod getHTTPMethod() {
		return HTTPMethod.GET;
	}

	@Override
	public String getURL() {
		return this.url;
	}

	@Override
	public String getProtocol() {
		return this.protocol;
	}

	@Override
	public Map<String, String> getHeaders() {
		return new HashMap<>(this.headers);
	}

	@Override
	public List<String> getBody() {
		return new ArrayList<>(this.body);
	}

}
