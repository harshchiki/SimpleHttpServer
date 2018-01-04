package httprequestclient;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class HTTPRequestClient {

	private final String USER_AGENT = "Mozilla/5.0";

	public static void main(String[] args) throws Exception {

		HTTPRequestClient http = new HTTPRequestClient();

		System.out.println("Sending POST");
		http.sendPost();

		System.out.println("Sending PUT");
		http.sendPut();

		System.out.println("Sending Delete");
		http.sendDELETE();
	}



	// HTTP POST request
	private void sendPost() throws Exception {
		send("POST");
	}

	// HTTP PUT request
	private void sendPut() throws Exception {
		send("PUT");
	}

	// HTTP DELETE request
	private void sendDELETE() throws Exception {
		send("DELETE");
	}


	private void send(final String method) throws MalformedURLException, IOException, ProtocolException {
		URL url = new URL("http://localhost:8086/" + method + "request");
		HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
		httpCon.setDoOutput(true);
		httpCon.setRequestMethod("POST");
		OutputStreamWriter out = new OutputStreamWriter(
				httpCon.getOutputStream());
		System.out.println(httpCon.getResponseCode());
		System.out.println(httpCon.getResponseMessage());
		out.close();
	}


}
