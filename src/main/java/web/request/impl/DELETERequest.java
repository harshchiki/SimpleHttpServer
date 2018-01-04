package web.request.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import web.request.HTTPRequest;
import web.request.enums.HTTPMethod;
//TO-DO: implement toString()
public class DELETERequest extends HTTPRequestBase implements HTTPRequest{
	private final String url;
	private final String protocol;
	private final Map<String, String> headers;
	private final List<String> body;
	
	private final static Logger logger = Logger.getLogger(DELETERequest.class);
	
	

	public DELETERequest(final String url, final String protocol, final Map<String, String> headers, final List<String> body) {
		this.url = url;
		this.protocol = protocol;
		this.headers = headers;
		this.body = body;
		
		logRequestAttributes(this.url, protocol, headers, body, HTTPMethod.DELETE, logger);
	}

	@Override
	public String toString(){
		return serializeRequestAsString(this.url, this.protocol, this.headers, this.body, HTTPMethod.DELETE);
	}
	
	@Override
	public HTTPMethod getHTTPMethod() {
		return HTTPMethod.DELETE;
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
