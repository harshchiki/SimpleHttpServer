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
		final String actualResponse = callServer(postURL, method);
		final String expectedResponse = "This is POST response";

		// VERIFY
		if(!actualResponse.equals(expectedResponse)){
			System.out.println("RESPONSE NOT AS EXPECTED for postURL: " + postURL);
			System.out.println("Actual: " + actualResponse);
			System.out.println("Expected: " + expectedResponse);
		} else { 
			System.out.println("Output matches for postURL: " + postURL);
			System.out.println("Output: " + actualResponse);
		}


		System.out.println("#############################################");
		System.out.println("\n\n");
	}

	// HTTP PUT request
	private void sendPut() throws Exception {
		final String method = "PUT";
		final URL putURL = new URL("http://localhost:8086/" + method + "request");
		final String actualResponse = callServer(putURL, method);
		final String expectedResponse = "This is PUT response";

		// VERIFY
		if(!actualResponse.equals(expectedResponse)){
			System.out.println("RESPONSE NOT AS EXPECTED for putURL: " + putURL);
			System.out.println("Actual: " + actualResponse);
			System.out.println("Expected: " + expectedResponse);
		} else { 
			System.out.println("Output matches for putURL: " + putURL);
			System.out.println("Output: " + actualResponse);
		}

		System.out.println("#############################################");
		System.out.println("\n\n");
	}

	// HTTP DELETE request
	private void sendDELETE() throws Exception {
		final String method = "DELETE";
		final URL deleteURL = new URL("http://localhost:8086/" + method + "request");
		final String actualResponse = callServer(deleteURL, method);
		final String expectedResponse = "This is DELETE response";

		// VERIFY
		if(!actualResponse.equals(expectedResponse)){
			System.out.println("RESPONSE NOT AS EXPECTED for deleteURL: " + deleteURL);
			System.out.println("Actual: " + actualResponse);
			System.out.println("Expected: " + expectedResponse);
		} else { 
			System.out.println("Output matches for putURL: " + deleteURL);
			System.out.println("Output: " + actualResponse);
		}
		System.out.println("#############################################");
		System.out.println("\n\n");
	}

	// HTTP GET request
	private void sendGET() throws Exception {
		final String firstURL = "http://localhost:8086/first.html";
		final String actualFirstURLResponseHTML = makeGETRequest("http://localhost:8086/first.html");
		final String expectedFirstURLResponseHTML = "<html>	<body>		<h3> First Page</h3>	</body></html>";
		if(!actualFirstURLResponseHTML.equals(expectedFirstURLResponseHTML)){
			System.out.println("RESPONSE NOT AS EXPECTED for firstURL: " + firstURL);
			System.out.println("Actual: " + actualFirstURLResponseHTML);
			System.out.println("Expected: " + expectedFirstURLResponseHTML);
		} else { 
			System.out.println("Output matches for firstURL: " + firstURL);
			System.out.println("Output: " + actualFirstURLResponseHTML);
		}
		System.out.println();

		final String secondURL = "http://localhost:8086/second.html";
		final String expectedSecondURLResponseHTML = "<html>	<body>		<h3> Second Page</h3>	</body></html>";
		final String actualSecondURLResponseHTML = makeGETRequest(secondURL);
		if(!actualSecondURLResponseHTML.equals(expectedSecondURLResponseHTML)){
			System.out.println("RESPONSE NOT AS EXPECTED for secondURL: " + secondURL);
			System.out.println("Actual: " + actualSecondURLResponseHTML);
			System.out.println("Expected: " + expectedSecondURLResponseHTML);
		} else { 
			System.out.println("Output matches for secondURL: " + secondURL);
			System.out.println("Output: " + actualSecondURLResponseHTML);
		}
		System.out.println("#############################################");
		System.out.println("\n\n");
	}

	private String callServer(final URL url, final String method) throws Exception {
		System.out.println(method + ": " + url);
		final HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
		httpCon.setDoOutput(true);
		httpCon.setRequestMethod(method);
		final OutputStreamWriter out = new OutputStreamWriter(
				httpCon.getOutputStream());
		System.out.println("Response Code: " + httpCon.getResponseCode());
		System.out.println("Message: " + httpCon.getResponseMessage());

		InputStream inputStream = httpCon.getInputStream();

		final Scanner inScanner = new Scanner(inputStream);
		final StringBuilder responseBuilder = new StringBuilder();
		while(inScanner.hasNext()){
			responseBuilder.append(inScanner.nextLine());
		}

		out.close();
		inScanner.close();
		return responseBuilder.toString();
	}



	private String makeGETRequest(String urlToRead) throws Exception {
		final StringBuilder result = new StringBuilder();
		final URL url = new URL(urlToRead);
		final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line;
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		rd.close();
		return result.toString();
	}

}
