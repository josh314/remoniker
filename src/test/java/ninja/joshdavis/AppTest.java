package ninja.joshdavis;

import static org.junit.Assert.*;
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
    public void FileTextAreaTest() {
        FileTextArea srcFileTextArea= new FileTextArea();
        assertNotNull(srcFileTextArea);
    }
    @Test
    public void EditorTest() {
        Editor mrEd = new Editor();
        assertNotNull(mrEd);
        mrEd.setSearchString("foo");
        mrEd.setReplaceString("baz");

        String str = mrEd.edit("foobarfoo");
        assertEquals(str, "bazbarfoo");

        mrEd.setGlobalSearch(true);
        str = mrEd.edit("foobarfoo");
        assertEquals(str, "bazbarbaz");

        mrEd.setCaseInsensitiveSearch(true);
        str = mrEd.edit("FoobarfOo");
        assertEquals(str, "bazbarbaz");

        mrEd.setCaseInsensitiveSearch(false);
        mrEd.setLiteralSearch(false);
        mrEd.setSearchString("f.o");
        str = mrEd.edit("foobarfao");
        assertEquals(str, "bazbarbaz");
    }
   
}
