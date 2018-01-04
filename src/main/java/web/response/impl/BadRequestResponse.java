package web.response.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import web.response.HTTPResponse;
import web.response.constants.ContentType;
import web.response.constants.HTTPStatusCode;

public class BadRequestResponse implements HTTPResponse{
	private final String httpStatusCode = HTTPStatusCode.BAD_REQUEST;
	private final String contentType = ContentType.HTML;
	private final Map<String, String> header = new HashMap<>();
	private final String BAD_REQUEST_RESPONSE_HTML = "<html><body><b>" + this.httpStatusCode + "</body></html>";
	private final byte[] body = this.BAD_REQUEST_RESPONSE_HTML.getBytes(); 

	
	// Private methods
	private void setDate(final Date date) { 
		header.put("Date", date.toString());
	}
	
	public void setContentLength(final long value) {
		header.put("Content-Length", String.valueOf(value));
	}

	public void setContentType(final String value) {
		header.put("Content-Type", value);
	}
	
	public BadRequestResponse(){
		this.buildResponse();
	}
	
	private void buildResponse() {
		setDate(new Date());
		setContentLength(this.BAD_REQUEST_RESPONSE_HTML.length());
		setContentType(ContentType.HTML);
	}
	
	// Contract implementation

	@Override
	public String getHTTPStatusCode() {
		return this.httpStatusCode;
	}

	@Override
	public String getContentType() {
		return this.contentType;
	}

	@Override
	public Map<String, String> getHeader() {
		return new HashMap<>(this.header);
	}

	@Override
	public byte[] getbody() {
		return this.body;
	}

	@Override
	public String getResponseString() {
		final StringBuilder responseBuilder = new StringBuilder();
		
		responseBuilder.append(this.protocol + " " + this.httpStatusCode + "\r\n\r\n" + this.BAD_REQUEST_RESPONSE_HTML);
		
		return responseBuilder.toString();
	}

}
