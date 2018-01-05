package httprequestclient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class HTTPRequestClient {

	private final String USER_AGENT = "Mozilla/5.0";

	public static void main(String[] args) throws Exception {

		HTTPRequestClient http = new HTTPRequestClient();

		System.out.println("Sending GET");
		http.sendGET();

		System.out.println("Sending POST");
		http.sendPost();

		System.out.println("Sending PUT");
		http.sendPut();

		System.out.println("Sending DELETE");
		http.sendDELETE();
	}



	// HTTP POST request
	private void sendPost() throws Exception {
		final String method = "POST";
		final URL postURL = new URL("http://localhost:8086/" + method + "request");
		callServer(postURL, method);
	}

	// HTTP PUT request
	private void sendPut() throws Exception {
		final String method = "PUT";
		final URL putURL = new URL("http://localhost:8086/" + method + "request");
		callServer(putURL, method);
	}

	// HTTP DELETE request
	private void sendDELETE() throws Exception {
		final String method = "DELETE";
		final URL deleteURL = new URL("http://localhost:8086/" + method + "request");
		callServer(deleteURL, method);
	}

	// HTTP GET request
	private void sendGET() throws Exception {
		final String firstURL = "http://localhost:8086/first.html";
		System.out.println(makeGETRequest("http://localhost:8086/first.html"));

		System.out.println();

		final String secondURL = "http://localhost:8086/second.html";
		System.out.println(makeGETRequest(secondURL));

	}

	private void callServer(final URL url, final String method) throws Exception {
		System.out.println(method + ": " + url);
		HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
		httpCon.setDoOutput(true);
		httpCon.setRequestMethod(method);
		OutputStreamWriter out = new OutputStreamWriter(
				httpCon.getOutputStream());
		System.out.println("Response Code: " + httpCon.getResponseCode());
		System.out.println("Message: " + httpCon.getResponseMessage());

		System.out.println("\nLogging input stream");
		InputStream inputStream = httpCon.getInputStream();
		
		Scanner inScanner = new Scanner(inputStream);
		while(inScanner.hasNext()){
			String content = inScanner.nextLine();
			System.out.println(content);
		}


		out.close();
		System.out.println("#############################################");
	}


	
	private String makeGETRequest(String urlToRead) throws Exception {
	      StringBuilder result = new StringBuilder();
	      URL url = new URL(urlToRead);
	      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	      conn.setRequestMethod("GET");
	      BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	      String line;
	      while ((line = rd.readLine()) != null) {
	         result.append(line);
	      }
	      rd.close();
	      return result.toString();
	   }
	
}
