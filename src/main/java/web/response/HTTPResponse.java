package web.response;

import java.util.Map;

import web.response.constants.ContentType;
import web.response.constants.HTTPStatusCode;

public interface HTTPResponse {
	void buildResponse();
	String getHTTPStatusCode();
	String getContentType();
	Map<String, String> getHeader();
	byte[] getbody();
}
