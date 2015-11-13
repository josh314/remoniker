package ninja.joshdavis;

import static org.junit.Assert.assertNotNull;
import org.junit.Test;

/**
 * Unit test for App.
 */
public class AppTest {
    @Test
    public void AppFrameTest() {
        AppFrame frame = new AppFrame();
        assertNotNull(frame);
    }
    @Test
    public void SrcTextAreaTest() {
        SrcTextArea srcTextArea= new SrcTextArea();
        assertNotNull(srcTextArea);
    }
}
