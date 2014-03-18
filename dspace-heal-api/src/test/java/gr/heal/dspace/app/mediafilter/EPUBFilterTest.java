/**
 * 
 */
package gr.heal.dspace.app.mediafilter;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author aanagnostopoulos
 * 
 */
public class EPUBFilterTest{

	/** log4j category */
	private static final Logger log = Logger.getLogger(EPUBFilterTest.class);

	/**
	 * This method will be run before every test as per @Before. It will
	 * initialize resources required for the tests.
	 * 
	 * Other methods can be annotated with @Before here or in subclasses but no
	 * execution order is guaranteed
	 */
	@Before
	public void init() {
	}

	/**
	 * This method will be run after every test as per @After. It will clean
	 * resources initialized by the @Before methods.
	 * 
	 * Other methods can be annotated with @After here or in subclasses but no
	 * execution order is guaranteed
	 */
	@After
	public void destroy() {
	}

	@Test
	public void testEPUBFilter() {

		EPUBFilter epubFilter = new EPUBFilter();
		try {
			InputStream destinationStream = epubFilter
					.getDestinationStream(new FileInputStream(
							"/home/aanagnostopoulos/Downloads/Christine.epub"));

			assertNotNull(destinationStream);
			
		} catch (Exception e) {
			log.error(e);
			fail();
		}

	}

}
