package web.response.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import web.response.HTTPResponse;
import web.response.constants.ContentType;
import web.response.constants.HTTPStatusCode;

public class PUTResponse implements HTTPResponse {
	private final String httpStatusCode = HTTPStatusCode.OK;
	private final String contentType = ContentType.TEXT;
	private final Map<String, String> header = new HashMap<>();
	private byte[] body = null;

	public PUTResponse(){
		this.buildResponse();
	}
	
	private void buildResponse() {

	}

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
		if(null == body) {
			return this.body;
		}
		
		final byte[] bodyOfRequest = new byte[this.body.length];
		System.arraycopy(this.body, 0, bodyOfRequest, 0, this.body.length);
		return bodyOfRequest;
	}

	@Override
	public String getResponseString() {
		return this.protocol + " " + this.httpStatusCode + "\r\n\r\n" + new Date();
	}

}
