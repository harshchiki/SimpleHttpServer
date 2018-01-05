package httpserver.multi_threaded_webserver;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class WebServerTest extends TestCase
{
	/**
	 * Create the test case
	 *
	 * @param testName name of the test case
	 */
	public WebServerTest( String testName ) {
		super( testName );
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite( WebServerTest.class );
	}

	/**
	 * Rigourous Test :-)
	 */
	public void testApp() {
		assertTrue( true );
	}
}
