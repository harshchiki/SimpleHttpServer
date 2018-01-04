package web.response.constants;

/*
 * List of HTTP Status Codes:
 * https://en.wikipedia.org/wiki/List_of_HTTP_status_codes
 */
public class HTTPStatusCode {
	// 2xx Success
	public static final String OK = "200 OK";
	public static final String CREATED = "201 Created";
	public static final String ACCEPTED = "202 Accepted";
	public static final String NON_AUTHORITIVE_INFORMATION = "203 Non-Authoritative Information (since HTTP/1.1)";
	public static final String NO_CONTENT = "204 No Content";
	public static final String RESET_CONTENT = "205 Reset Content";
	public static final String PARTIAL_CONTENT = "206 Partial Content";
	public static final String MULTI_STATUS = "207 Multi-Status";
	public static final String ALREADY_REPORTED = "208 Already Reported";
	public static final String IM_USED = "226 IM Used";
	
	
	// 4xx Client Errors
	public static final String BAD_REQUEST = "400 Bad Request";
	public static final String UNAUTHORISED = "401 Unauthorized"; 
	public static final String PAYMENT_REQUIRED = "402 Payment Required";
	public static final String FORBIDDEN = "403 Forbidden";
	public static final String NOT_FOUND = "404 Not Found";
	public static final String METHOD_NOT_ALLOWED = "405 Method Not Allowed";
	public static final String NOT_ACCEPTABLE = "406 Not Acceptable";
	public static final String PROXY_AUTHENTICATION_REQUIRED = "407 Proxy Authentication Required"; 
	public static final String REQUEST_TIMEOUT = "408 Request Timeout";
	// there are more
	
	
	// 5xx Server errors
	public static final String INTERNAL_SERVER_ERROR = "500 Internal Server Error";
	public static final String NOT_IMPLEMENTED = "501 Not Implemented";
	public static final String BAD_GATEWAY = "502 Bad Gateway";
	public static final String SERVICE_UNAVAILABLE = "503 Service Unavailable";
	public static final String GATEWAY_TIMEOUT = "504 Gateway Timeout";
	public static final String HTTP_VERSION_NOT_SUPPORTED = "505 HTTP Version Not Supported";
	public static final String VARIANT_ALSO_NEGOTIATES = "506 Variant Also Negotiates";
	public static final String INSUFFICIENT_STORAGE = "507 Insufficient Storage";
	public static final String LOOP_DETECTED = "508 Loop Detected";
	public static final String NOT_EXTENDED = "510 Not Extended";
	public static final String NETWORK_AUTHENTICATION_REQUIRED = "511 Network Authentication Required";
}
