package httpserver.multi_threaded_webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.Scanner;

import org.junit.Test;

import junit.framework.TestCase;
import web.connection.Connection;

/*
 * References:
 * https://stackoverflow.com/questions/40414930/mocking-a-server-client-connection-with-mockito
 */
public class WebServerTest extends TestCase
{
	private ServerSocket serverSocket;
	private final String rootPath = "Content/";

	public void tearDown(  ) {
		synchronized(serverSocket){
			if(serverSocket != null){
				try {
					if(!serverSocket.isClosed()) {
						serverSocket.close();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				serverSocket = null;
			}
		}

	}

	public void setUp() {
		try {
			if(serverSocket == null) {
				serverSocket = new ServerSocket(3000);
			}else{
				synchronized(serverSocket) {
					serverSocket = new ServerSocket(3000);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		Thread server = new Thread(new Runnable(){

			@Override
			public void run() {
				Connection conn;
				try {
					conn = new Connection(serverSocket.accept(), rootPath);
					conn.run();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

		});
		server.start();
	}

	@Test
	public void testGETFirstURL() throws Exception {
		final String actualFirstURLResponseHTML = makeGETRequest("http://localhost:3000/first.html");
		final String expectedFirstURLResponseHTML = "<html>	<body>		<h3> First Page</h3>	</body></html>";
		assertEquals(expectedFirstURLResponseHTML, actualFirstURLResponseHTML);
	}

	public void testGETSecondURL() throws Exception {
		final String actualSecondURLResponseHTML = makeGETRequest("http://localhost:3000/second.html");
		final String expectedSecondURLResponseHTML = "<html>	<body>		<h3> Second Page</h3>	</body></html>";
		assertEquals(expectedSecondURLResponseHTML, actualSecondURLResponseHTML);
	}

	public void testPOST() throws Exception{
		final String method = "POST";
		final URL postURL = new URL("http://localhost:3000/" + method + "request");
		final String actualResponse = callServer(postURL, method);
		final String expectedResponse = "This is POST response";
		
		assertEquals(expectedResponse, actualResponse);
	}

	public void testPUT()  throws Exception {
		final String method = "PUT";
		final URL postURL = new URL("http://localhost:3000/" + method + "request");
		final String actualResponse = callServer(postURL, method);
		final String expectedResponse = "This is PUT response";
		
		assertEquals(expectedResponse, actualResponse);
	}

	public void testDELETE()  throws Exception {
		final String method = "DELETE";
		final URL postURL = new URL("http://localhost:3000/" + method + "request");
		final String actualResponse = callServer(postURL, method);
		final String expectedResponse = "This is DELETE response";
		
		assertEquals(expectedResponse, actualResponse);
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
	
	private String callServer(final URL url, final String method) throws Exception {
		final HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
		httpCon.setDoOutput(true);
		httpCon.setRequestMethod(method);
		final OutputStreamWriter out = new OutputStreamWriter(
				httpCon.getOutputStream());

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
}
