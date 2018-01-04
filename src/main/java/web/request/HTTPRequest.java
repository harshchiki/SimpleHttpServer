package web.request;

import java.util.List;
import java.util.Map;

import web.request.enums.HTTPMethod;

public interface HTTPRequest {
	HTTPMethod getHTTPMethod();
	String getURL();
	String getProtocol();
	Map<String, String> getHeaders();
	List<String> getBody();
	
}
