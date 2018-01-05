package web.response.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import web.response.HTTPResponse;
import web.response.constants.ContentType;
import web.response.constants.HTTPStatusCode;

/*
 * looks for the queried file with the provided rootPath and requestURL
 * If found: write back the file on the outputstream of the socket
 * else:  404 Not Found error to be thrown back
 */
public class GETFileResponse implements HTTPResponse{
	private final String rootPath, requestURL;
	private String httpStatusCode;
	private String contentType = ContentType.HTML;
	private final Map<String, String> headers = new HashMap<>();
	private byte[] body = null;
	
	private final static Logger logger = Logger.getLogger(GETFileResponse.class);
	
	public GETFileResponse(final String rootPath, final String requestURL) {
		this.rootPath = rootPath;
		this.requestURL = requestURL;
		this.buildResponse();
	}

	
	// Contract implementation
	private void buildResponse() {
		logger.info("Building Response: Query for file at root: "
				+ rootPath + " location: " + this.requestURL 
				+ ". Complete URL: " + this.rootPath + this.requestURL);
		setDate(new Date());
		final File queriedFile = new File(this.rootPath+this.requestURL);
		if(!queriedFile.exists()){
			this.httpStatusCode = HTTPStatusCode.NOT_FOUND;
			final String FILE_NOT_FOUND_HTML = "<html><body>File " + queriedFile + " not found.</body></html>";
			setContentLength(FILE_NOT_FOUND_HTML.length());
			setContentType(ContentType.HTML);
			this.body = FILE_NOT_FOUND_HTML.getBytes();
		}else{
			// file exists
			this.httpStatusCode = HTTPStatusCode.OK;
			
			try {
				final FileInputStream reader = new FileInputStream(queriedFile);
				int length = reader.available();
				body = new byte[length];
				reader.read(body);
				reader.close();
				setContentLength(length);
				
				if (queriedFile.getName().endsWith(".htm") || queriedFile.getName().endsWith(".html")) {
					setContentType(ContentType.HTML);
				} else {
					setContentType(ContentType.TEXT);
				}
			} catch (FileNotFoundException e) {
				this.httpStatusCode = HTTPStatusCode.NOT_FOUND;
				logger.error("Requested file not found: " + queriedFile.getName());
				logger.error("Error: " + e.getMessage());
			} catch (IOException e1) {
				this.httpStatusCode = HTTPStatusCode.INTERNAL_SERVER_ERROR;
				logger.error("IOException occurred while reading file: " + queriedFile.getName());
			}
		}
		
		logger.info("File Response Built");
		logger.info(this.toString());
	}

	@Override
	public String toString() {
		final StringBuilder headerStringBuilder = new StringBuilder();
		
		headerStringBuilder.append("\nResponse Header");
		headers.entrySet().stream().forEach(entry -> headerStringBuilder.append("\n" + entry.getKey() + ": " + entry.getValue()));
		headerStringBuilder.append("\n");
		
		final StringBuilder bodyBuilder = new StringBuilder();
		for(byte b : body){
			bodyBuilder.append((char)b);
		}
		
		return "\n\nFileResponse [rootPath=" + rootPath 
				+ ", requestURL=" + requestURL 
				+ ", httpStatusCode=" + httpStatusCode 
				+ ", contentType=" + contentType 
				+ ", protocol=" + protocol 
				+ ", headers=" + headerStringBuilder.toString()
				+ ", body=" + bodyBuilder.toString() + "]\n\n";
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
		return new HashMap<>(this.headers);
	}

	@Override
	public byte[] getbody() {
		byte[] bodyOfResponse = new byte[this.body.length];
		System.arraycopy(this.body,0, bodyOfResponse, 0, this.body.length);
		return bodyOfResponse;
	}
	
	
	// PRIVATE METHODS
	public void setDate(final Date date) {
		headers.put("Date", date.toString());
	}

	public void setContentLength(final long value) {
		headers.put("Content-Length", String.valueOf(value));
	}

	public void setContentType(final String value) {
		headers.put("Content-Type", value);
	}


	@Override
	public String getResponseString() {
		final StringBuilder responseBuilder = new StringBuilder();
		
		final StringBuilder bodyBuilder = new StringBuilder();
		for(byte b : body){
			bodyBuilder.append((char)b);
		}
		
		responseBuilder.append(this.protocol + " " + this.httpStatusCode + "\r\n\r\n" + bodyBuilder.toString());
		
		return responseBuilder.toString();
	}
}